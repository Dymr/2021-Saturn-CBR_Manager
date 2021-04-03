from django.shortcuts import get_object_or_404
from rest_framework import serializers

from goals.utils import differentiate_key_value, update_object
from goals.models import Goal


class GoalSerializer(serializers.ModelSerializer):

    class Meta:
        model = Goal
        fields = "__all__"

    def create(self, validated_data):
        return Goal.objects.create(user_creator=validated_data['user_creator'],
                    
                                    category=validated_data["category"],
                                    title=validated_data["title"],
                                    description=validated_data["description"],
                                    is_initial_goal=validated_data["is_initial_goal"],
                                    status=validated_data["status"],
                                    client_id=validated_data["client_id"]
                                    )
                                    


    def update(self, instance, validated_data):
        self.update_goal_on_put(instance, validated_data)

        return instance
        
    def update_goal_on_put(self, goal_instance, validated_data):
        for key, value in validated_data.items():
            setattr(goal_instance, key, value)
        goal_instance.save()