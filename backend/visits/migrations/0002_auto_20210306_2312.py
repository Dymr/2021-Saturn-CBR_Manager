# Generated by Django 3.1.5 on 2021-03-06 23:12

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('visits', '0001_initial'),
    ]

    operations = [
        migrations.AlterField(
            model_name='visit',
            name='location_drop_down',
            field=models.CharField(max_length=100),
        ),
    ]
