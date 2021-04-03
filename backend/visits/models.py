from django.contrib.auth.models import User
from django.db import models
from django.utils import timezone


from clients.models import Client
from tools.models import TimestampedModel


class Visit(TimestampedModel):
    user_creator = models.ForeignKey(User, on_delete=models.CASCADE)
    client = models.ForeignKey(Client, on_delete=models.CASCADE)

    client_state_previous = models.JSONField(blank=True, null=True, editable=False)
    client_state_updated = models.JSONField(blank=True, null=True, editable=False)

    is_cbr_purpose = models.BooleanField(blank=True, default=False)
    is_disability_referral_purpose = models.BooleanField(blank=True, default=False)
    is_disability_follow_up_purpose = models.BooleanField(blank=True, default=False)

    is_health_provision = models.BooleanField(blank=True, default=False)
    is_education_provision = models.BooleanField(blank=True, default=False)
    is_social_provision = models.BooleanField(blank=True, default=False)

    cbr_worker_name = models.CharField(blank=True, max_length=30)

    location_visit_gps = models.CharField(blank=True, max_length=30)
    location_drop_down = models.CharField(blank=True, max_length=100)
    village_no_visit = models.IntegerField(blank=True, default=0)

    wheelchair_health_provision = models.BooleanField(blank=True, default=False)
    prosthetic_health_provision = models.BooleanField(blank=True, default=False)
    orthotic_health_provision = models.BooleanField(blank=True, default=False)
    repairs_health_provision = models.BooleanField(blank=True, default=False)
    referral_health_provision = models.BooleanField(blank=True, default=False)
    advice_health_provision = models.BooleanField(blank=True, default=False)
    advocacy_health_provision = models.BooleanField(blank=True, default=False)
    encouragement_health_provision = models.BooleanField(blank=True, default=False)

    wheelchair_health_provision_text = models.CharField(blank=True, max_length=100)
    prosthetic_health_provision_text = models.CharField(blank=True, max_length=100)
    orthotic_health_provision_text = models.CharField(blank=True, max_length=100)
    repairs_health_provision_text = models.CharField(blank=True, max_length=100)
    referral_health_provision_text = models.CharField(blank=True, max_length=100)
    advice_health_provision_text = models.CharField(blank=True, max_length=100)
    advocacy_health_provision_text = models.CharField(blank=True, max_length=100)
    encouragement_health_provision_text = models.CharField(blank=True, max_length=100)

    conclusion_health_provision = models.CharField(blank=True, max_length=255)

    advice_education_provision = models.BooleanField(blank=True, default=False)
    advocacy_education_provision = models.BooleanField(blank=True, default=False)
    referral_education_provision = models.BooleanField(blank=True, default=False)
    encouragement_education_provision = models.BooleanField(blank=True, default=False)

    advice_education_provision_text = models.CharField(blank=True, max_length=100)
    advocacy_education_provision_text = models.CharField(blank=True, max_length=100)
    referral_education_provision_text = models.CharField(blank=True, max_length=100)
    encouragement_education_provision_text = models.CharField(blank=True, max_length=100)

    conclusion_education_provision = models.CharField(blank=True, max_length=255)


    advice_social_provision = models.BooleanField(blank=True, default=False)
    advocacy_social_provision = models.BooleanField(blank=True, default=False)
    referral_social_provision = models.BooleanField(blank=True, default=False)
    encouragement_social_provision = models.BooleanField(blank=True, default=False)

    advice_social_provision_text = models.CharField(blank=True, max_length=100)
    advocacy_social_provision_text = models.CharField(blank=True, max_length=100)
    referral_social_provision_text = models.CharField(blank=True, max_length=100)
    encouragement_social_provision_text = models.CharField(blank=True, max_length=100)

    conclusion_social_provision = models.CharField(blank=True, max_length=255)

    # contains the specific info that was changed
    client_info_changed = models.JSONField(blank=True, null=True, editable=False)

    additional_notes = models.TextField(verbose_name="additional notes", blank=True, null=True, default="")

    def __str__(self):
        return "Visit {} - {} {}".format(self.id, self.client.first_name, self.client.last_name)
