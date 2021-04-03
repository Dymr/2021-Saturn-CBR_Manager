from django.contrib.auth.models import User
from django.db import models
from django.utils import timezone

from clients.models import Client

class Goal(models.Model):
    datetime_created = models.DateTimeField(auto_now_add=True)
    datetime_completed = models.DateTimeField(blank=True, null=True)
    user_creator = models.ForeignKey(User, on_delete=models.SET_NULL, null=True)
    client_id = models.IntegerField(blank=True, default=0)

    category = models.CharField(max_length=30)
    title = models.CharField(max_length=100)
    description = models.TextField()
    is_initial_goal = models.BooleanField(blank=True, default=False)
    status = models.CharField(max_length=30)

    # contains the specific info that was changed
    client_info_changed = models.JSONField(blank=True, null=True, editable=False)

    def __str__(self):
        return "Goal {} {} for clientId {} - status: {}".format(self.id, self.title, 
        self.client_id, self.status)
