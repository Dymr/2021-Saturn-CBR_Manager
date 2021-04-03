from django.shortcuts import get_object_or_404
from rest_framework import serializers

from clients.models import Client
from clients.serializer import ClientSerializer
from visits.models import Visit
from visits.utils import differentiate_key_value, update_object


class VisitSerializer(serializers.ModelSerializer):
    client_id = serializers.IntegerField(required=True)
    client = ClientSerializer(required=True)

    class Meta:
        model = Visit
        fields = "__all__"

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
                                    client_info_changed=client_info_changed_json,
                    
                                    is_cbr_purpose=validated_data["is_cbr_purpose"],
                                    is_disability_referral_purpose=validated_data["is_disability_referral_purpose"],
                                    is_disability_follow_up_purpose=validated_data["is_disability_follow_up_purpose"],
                                    is_health_provision=validated_data["is_health_provision"],
                                    is_education_provision=validated_data["is_education_provision"],
                                    is_social_provision=validated_data["is_social_provision"],
                                    cbr_worker_name=validated_data["cbr_worker_name"],
                                    location_visit_gps=validated_data["location_visit_gps"],
                                    location_drop_down=validated_data["location_drop_down"],
                                    village_no_visit=validated_data["village_no_visit"],
                                    wheelchair_health_provision=validated_data["wheelchair_health_provision"],
                                    prosthetic_health_provision=validated_data["prosthetic_health_provision"],
                                    orthotic_health_provision=validated_data["orthotic_health_provision"],
                                    repairs_health_provision=validated_data["repairs_health_provision"],
                                    referral_health_provision=validated_data["referral_health_provision"],
                                    advice_health_provision=validated_data["advice_health_provision"],
                                    advocacy_health_provision=validated_data["advocacy_health_provision"],
                                    encouragement_health_provision=validated_data["encouragement_health_provision"],
                                    wheelchair_health_provision_text=validated_data["wheelchair_health_provision_text"],
                                    prosthetic_health_provision_text=validated_data["prosthetic_health_provision_text"],
                                    orthotic_health_provision_text=validated_data["orthotic_health_provision_text"],
                                    repairs_health_provision_text=validated_data["repairs_health_provision_text"],
                                    referral_health_provision_text=validated_data["referral_health_provision_text"],
                                    advice_health_provision_text=validated_data["advice_health_provision_text"],
                                    advocacy_health_provision_text=validated_data["advocacy_health_provision_text"],
                                    encouragement_health_provision_text=validated_data["encouragement_health_provision_text"],
                                    conclusion_health_provision=validated_data["conclusion_health_provision"],
                                    advice_education_provision=validated_data["advice_education_provision"],
                                    advocacy_education_provision=validated_data["advocacy_education_provision"],
                                    referral_education_provision=validated_data["referral_education_provision"],
                                    encouragement_education_provision=validated_data["encouragement_education_provision"],
                                    advice_education_provision_text=validated_data["advice_education_provision_text"],
                                    advocacy_education_provision_text=validated_data["advocacy_education_provision_text"],
                                    referral_education_provision_text=validated_data["referral_education_provision_text"],
                                    encouragement_education_provision_text=validated_data["encouragement_education_provision_text"],
                                    conclusion_education_provision=validated_data["conclusion_education_provision"],
                                    advice_social_provision=validated_data["advice_social_provision"],
                                    advocacy_social_provision=validated_data["advocacy_social_provision"],
                                    referral_social_provision=validated_data["referral_social_provision"],
                                    encouragement_social_provision=validated_data["encouragement_social_provision"],
                                    advice_social_provision_text=validated_data["advice_social_provision_text"],
                                    advocacy_social_provision_text=validated_data["advocacy_social_provision_text"],
                                    referral_social_provision_text=validated_data["referral_social_provision_text"],
                                    encouragement_social_provision_text=validated_data["encouragement_social_provision_text"],
                                    conclusion_social_provision=validated_data["conclusion_social_provision"]
                                    )
                                    


    def update(self, instance, validated_data):
        client = instance.client

        self.update_client(client, validated_data)

        self.update_visit_on_put(instance, validated_data)

        return instance

    def update_client(self, client, validated_date):
        update_object(client, **validated_date.get("client"))

    def update_visit_on_put(self, visit_instance, validated_data):
        post_update_client_json = ClientSerializer(visit_instance.client).data

        visit_instance.client_state_updated = post_update_client_json
        visit_instance.client_info_changed = differentiate_key_value(post_update_client_json,
                                                                        visit_instance.client_state_previous)
        for key, value in validated_data.items():
            if key == 'client':
                client = get_object_or_404(Client, id=validated_data['client_id'])
                setattr(visit_instance, 'client', client)
            else:
                setattr(visit_instance, key, value)
        visit_instance.save()