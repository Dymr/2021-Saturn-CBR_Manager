# Generated by Django 3.1.5 on 2021-04-10 19:37

from django.db import migrations


class Migration(migrations.Migration):

    dependencies = [
        ('visits', '0004_auto_20210408_0429'),
    ]

    operations = [
        migrations.RemoveField(
            model_name='visit',
            name='latitude',
        ),
        migrations.RemoveField(
            model_name='visit',
            name='longitude',
        ),
    ]
