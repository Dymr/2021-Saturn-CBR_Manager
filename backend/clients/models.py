from django.db import models

from tools.models import TimestampedModel


class Client(TimestampedModel):
    """
    The clients that get visited by CBR members
    """
    # Client Basic information
    cbr_client_id = models.CharField(unique=True, editable=False, max_length=10)  # auto generated

    first_name = models.CharField(max_length=30)
    last_name = models.CharField(max_length=30)
    location = models.CharField(max_length=100, blank=True)
    gps_location = models.CharField(max_length=100, blank=True)
    consent = models.CharField(max_length=5, blank=True)
    village_no = models.IntegerField(default=0)
    gender = models.CharField(max_length=20, blank=True)
    age = models.IntegerField(default=0) # auto generated
    birthdate = models.DateTimeField(blank = True)
    contact_client = models.CharField(max_length=30, blank=True)
    care_present = models.CharField(max_length=5, blank=True)
    contact_care = models.CharField(max_length=30, blank=True)
    photo = models.ImageField(upload_to='images/', default='images/default.png')
    disability = models.CharField(max_length=250, blank=True)
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

    # Fields updated by visits
    goal_met_health_provision = models.TextField(blank=True, max_length=30)
    goal_met_education_provision = models.TextField(blank=True, max_length=30)
    goal_met_social_provision = models.TextField(blank=True, max_length=30)

   
    # Field updated by taking baseline survey
    taken_baseline_survey = models.BooleanField(default=False)

    # Fields storing user location
    latitude = models.FloatField(default=0)
    longitude = models.FloatField(default=0)

    class Meta:
        ordering = ['id']

    def __str__(self):
        return "{} {}".format(self.first_name, self.last_name)

    def save(self, *args, **kwargs):
        if not self.cbr_client_id:
            self.cbr_client_id = self._generate_cbr_client_id()
        
        if not age:
            self.age = self._generate_age()

        super(Client, self).save(*args, **kwargs)

    def full_name(self):
        return self.first_name + " " + self.last_name

    def _generate_cbr_client_id(self):
        def get_first_letter(text):
            if text and len(text) > 0:
                return text[0]
            return ""

        def append_number_if_not_one(text, num):
            if num != 1:
                return text + str(num)
            return text

        candidate_number = 1
        candidate_id = get_first_letter(self.first_name).lower() + get_first_letter(self.last_name).lower()
        while Client.objects.filter(cbr_client_id=append_number_if_not_one(candidate_id, candidate_number)).exists():
            candidate_number += 1

        return append_number_if_not_one(candidate_id, candidate_number)

    def _generate_age(self):
        today = date.today()
        return today.year - birthdate.year - ((today.month, today.day) < (birthdate.month, birthdate.day))


class ClientHistoryRecord(models.Model):
    date_created = models.DateTimeField(auto_now_add=True, editable=False)
    field = models.CharField(max_length=100, editable=False)
    old_value = models.TextField(editable=False)
    new_value = models.TextField(editable=False)
    client = models.ForeignKey(Client, on_delete=models.CASCADE, editable=False)
