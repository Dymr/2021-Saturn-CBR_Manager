# Generated by Django 3.1.5 on 2021-03-13 11:48

from django.db import migrations, models
import django.utils.timezone


class Migration(migrations.Migration):

    dependencies = [
        ('visits', '0002_auto_20210313_0340'),
    ]

    operations = [
        migrations.AlterField(
            model_name='visit',
            name='datetime_created',
            field=models.DateTimeField(verbose_name=django.utils.timezone.now),
        ),
    ]
