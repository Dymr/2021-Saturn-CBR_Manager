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
    contact_client = models.CharField(max_length=20)
    care_present = models.CharField(max_length=5)
    contact_care = models.CharField(max_length=20)
    photo = models.ImageField(upload_to='images/', default='images/default.png')
    disability = models.CharField(max_length=50)
    date = models.DateTimeField(auto_now_add=True)
    # Extra text field for Client information(Health/Education... etc)
    health_risk = models.IntegerField(default=0)
    health_require = models.TextField(blank=True)
    health_goal = models.TextField(blank=True)

    education_risk = models.IntegerField(default=0)
    education_require = models.TextField(blank=True)
    education_goal = models.TextField(blank=True)

    social_risk = models.IntegerField(default=0)
    social_require = models.TextField(blank=True)
    social_goal = models.TextField(blank=True)

    risk_score = models.IntegerField(editable=False, default=0)

    # Extra fields checking Sync states
    last_modified = models.DateTimeField(auto_now=True)
    is_new_client = models.BooleanField(blank=True, default=False)

    class Meta:
        ordering = ['id']

    def __str__(self):
        return "{} {}".format(self.first_name, self.last_name)

    def save(self, *args, **kwargs):
        self.risk_score = int(self.health_risk) + int(self.social_risk) + int(self.education_risk)
        super(Client, self).save(*args, **kwargs)
