# Generated by Django 3.1.5 on 2021-03-14 00:52

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('clients', '0007_auto_20210313_0746'),
    ]

    operations = [
        migrations.AlterField(
            model_name='client',
            name='disability',
            field=models.CharField(max_length=250),
        ),
    ]
