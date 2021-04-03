from django.core.validators import FileExtensionValidator
from django.db import models


class APK(models.Model):
    name = models.CharField(max_length=50)
    date_uploaded = models.DateTimeField(auto_now_add=True)
    file = models.FileField(upload_to='apk/', validators=[FileExtensionValidator(allowed_extensions=['apk'])])
