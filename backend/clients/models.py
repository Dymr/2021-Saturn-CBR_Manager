from django.db import models


# Create your models here.


class Client(models.Model):
    """
    The clients that get visited by CBR members
    """
    # Client Basic information
    first_name = models.CharField(max_length=30)
    last_name = models.CharField(max_length=30)
    location = models.CharField(max_length=100)
    gps_location = models.CharField(max_length=100, blank=True)
    consent = models.CharField(max_length=5)
    village_no = models.IntegerField(default=0)
    gender = models.CharField(max_length=20)
    age = models.IntegerField(default=0)
    contact_client = models.IntegerField(default=0)
    care_present = models.CharField(max_length=5)
    contact_care = models.IntegerField(default=0)
    photo = models.ImageField(upload_to='images/', default='images/default.png')
    disability = models.CharField(max_length=50)
    date = models.DateTimeField(auto_now_add=True)
    # Extra text field for Client information(Health/Education... etc)
    health_risk = models.IntegerField(max_length=30)
    health_require = models.TextField(blank=True)
    health_goal = models.TextField(blank=True)

    education_risk = models.IntegerField(max_length=30)
    education_require = models.TextField(blank=True)
    education_goal = models.TextField(blank=True)

    social_risk = models.IntegerField(max_length=30)
    social_require = models.TextField(blank=True)
    social_goal = models.TextField(blank=True)

    risk_score = models.IntegerField(editable=False, default=0)

    # Visits
    is_cbr_purpose = models.BooleanField(blank=True, default=False)
    is_disability_referral_purpose = models.BooleanField(blank=True, default=False)
    is_disability_follow_up_purpose = models.BooleanField(blank=True, default=False)

    is_health_provision = models.BooleanField(blank=True, default=False)
    is_education_provision = models.BooleanField(blank=True, default=False)
    is_social_provision = models.BooleanField(blank=True, default=False)

    cbr_worker_name = models.TextField(blank=True, max_length=30)

    location_visit_gps = models.TextField(blank=True, max_length=30)
    location_drop_down = models.TextField(blank=True, max_length=30)
    village_no_visit = models.IntegerField(blank=True, default=0)

    wheelchair_health_provision = models.BooleanField(blank=True, default=False)
    prosthetic_health_provision = models.BooleanField(blank=True, default=False)
    orthotic_health_provision = models.BooleanField(blank=True, default=False)
    repairs_health_provision = models.BooleanField(blank=True, default=False)
    referral_health_provision = models.BooleanField(blank=True, default=False)
    advice_health_provision = models.BooleanField(blank=True, default=False)
    advocacy_health_provision = models.BooleanField(blank=True, default=False)
    encouragement_health_provision = models.BooleanField(blank=True, default=False)

    wheelchair_health_provision_text = models.TextField(blank=True, max_length=100)
    prosthetic_health_provision_text = models.TextField(blank=True, max_length=100)
    orthotic_health_provision_text = models.TextField(blank=True, max_length=100)
    repairs_health_provision_text = models.TextField(blank=True, max_length=100)
    referral_health_provision_text = models.TextField(blank=True, max_length=100)
    advice_health_provision_text = models.TextField(blank=True, max_length=100)
    advocacy_health_provision_text = models.TextField(blank=True, max_length=100)
    encouragement_health_provision_text = models.TextField(blank=True, max_length=100)

    goal_met_health_provision = models.TextField(blank=True, max_length=30)
    conclusion_health_provision = models.TextField(blank=True, max_length=100)

    advice_education_provision = models.BooleanField(blank=True, default=False)
    advocacy_education_provision = models.BooleanField(blank=True, default=False)
    referral_education_provision = models.BooleanField(blank=True, default=False)
    encouragement_education_provision = models.BooleanField(blank=True, default=False)

    advice_education_provision_text = models.TextField(blank=True, max_length=100)
    advocacy_education_provision_text = models.TextField(blank=True, max_length=100)
    referral_education_provision_text = models.TextField(blank=True, max_length=100)
    encouragement_education_provision_text = models.TextField(blank=True, max_length=100)

    goal_met_education_provision = models.TextField(blank=True, max_length=30)
    conclusion_education_provision = models.TextField(blank=True, max_length=100)

    advice_social_provision = models.BooleanField(blank=True, default=False)
    advocacy_social_provision = models.BooleanField(blank=True, default=False)
    referral_social_provision = models.BooleanField(blank=True, default=False)
    encouragement_social_provision = models.BooleanField(blank=True, default=False)

    advice_social_provision_text = models.TextField(blank=True, max_length=100)
    advocacy_social_provision_text = models.TextField(blank=True, max_length=100)
    referral_social_provision_text = models.TextField(blank=True, max_length=100)
    encouragement_social_provision_text = models.TextField(blank=True, max_length=100)

    goal_met_social_provision = models.TextField(blank=True, max_length=100)
    conclusion_social_provision = models.TextField(blank=True, max_length=100)

    class Meta:
        ordering = ['id']

    def __str__(self):
        return "{} {}".format(self.first_name, self.last_name)

    def save(self, *args, **kwargs):
        self.risk_score = int(self.health_risk) + int(self.social_risk) + int(self.education_risk)
        super(Client, self).save(*args, **kwargs)
