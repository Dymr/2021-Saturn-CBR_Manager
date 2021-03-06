# Generated by Django 3.1.5 on 2021-04-10 19:37

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('clients', '0007_auto_20210408_0418'),
    ]

    operations = [
        migrations.AddField(
            model_name='client',
            name='latitude',
            field=models.DecimalField(decimal_places=6, default=0, max_digits=9),
        ),
        migrations.AddField(
            model_name='client',
            name='longitude',
            field=models.DecimalField(decimal_places=6, default=0, max_digits=9),
        ),
    ]
