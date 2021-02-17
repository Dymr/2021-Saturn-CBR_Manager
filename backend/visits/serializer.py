from django.shortcuts import get_object_or_404
from rest_framework import serializers

from clients.models import Client
from clients.serializer import ClientSerializer
from visits.models import Visit
from visits.utils import differentiate_key_value, update_object


class VisitSerializer(serializers.ModelSerializer):
    client_id = serializers.IntegerField(required=True)
    client = ClientSerializer(write_only=True, required=True)

    class Meta:
        model = Visit
        fields = ["id", "client_id", "datetime_created", "user_creator", "additional_notes", "client",
                  "client_info_changed"]

    def create(self, validated_data):
        client = get_object_or_404(Client, id=validated_data['client_id'])

        pre_update_client_json = ClientSerializer(client).data
        self.update_client(client, validated_data)
        post_update_client_json = ClientSerializer(client).data

        client_info_changed_json = differentiate_key_value(post_update_client_json, pre_update_client_json)

        return Visit.objects.create(user_creator=validated_data['user_creator'],
                                    client=client,
                                    additional_notes=validated_data['additional_notes'],
                                    client_state_previous=pre_update_client_json,
                                    client_state_updated=post_update_client_json,
                                    client_info_changed=client_info_changed_json)

    def update(self, instance, validated_data):
        client = instance.client

        self.update_client(client, validated_data)

        self.update_visit_on_put(instance, validated_data)

        return instance

    def update_client(self, client, validated_date):
        update_object(client, **validated_date.get("client"))

    def update_visit_on_put(self, visit_instance, validated_data):
        post_update_client_json = ClientSerializer(visit_instance.client).data

        # update what was changed from the original state
        visit_instance.client_state_updated = post_update_client_json
        visit_instance.client_info_changed = differentiate_key_value(post_update_client_json,
                                                                         visit_instance.client_state_previous)

        # update fields
        visit_instance.user_creator = validated_data.get("user_creator", visit_instance.user_creator)
        visit_instance.additional_notes = validated_data.get("additional_notes", visit_instance.additional_notes)
        visit_instance.save()
