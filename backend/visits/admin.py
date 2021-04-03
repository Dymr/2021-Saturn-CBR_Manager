from django.contrib import admin

# Register your models here.
from visits.models import Visit


@admin.register(Visit)
class VisitAdmin(admin.ModelAdmin):
    list_display = ('id', 'client', 'user_creator')
