# Generated by Django 3.1.5 on 2021-04-03 02:48

from django.conf import settings
from django.db import migrations, models
import django.db.models.deletion


class Migration(migrations.Migration):

    dependencies = [
        migrations.swappable_dependency(settings.AUTH_USER_MODEL),
        ('baseline_survey', '0002_auto_20210321_1630'),
    ]

    operations = [
        migrations.AlterField(
            model_name='baselinesurvey',
            name='user_creator',
            field=models.ForeignKey(null=True, on_delete=django.db.models.deletion.SET_NULL, to=settings.AUTH_USER_MODEL),
        ),
    ]
