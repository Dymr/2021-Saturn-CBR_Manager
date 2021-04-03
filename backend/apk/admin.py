from django.contrib import admin

# Register your models here.
from apk.models import APK


@admin.register(APK)
class ClientAdmin(admin.ModelAdmin):
    readonly_fields = ("date_uploaded",)
