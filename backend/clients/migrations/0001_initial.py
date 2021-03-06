# Generated by Django 3.1.5 on 2021-03-14 23:46

from django.db import migrations, models
import django.db.models.deletion


class Migration(migrations.Migration):

    initial = True

    dependencies = [
    ]

    operations = [
        migrations.CreateModel(
            name='Client',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('cbr_client_id', models.CharField(editable=False, max_length=10, unique=True)),
                ('first_name', models.CharField(max_length=30)),
                ('last_name', models.CharField(max_length=30)),
                ('location', models.CharField(max_length=100)),
                ('gps_location', models.CharField(blank=True, max_length=100)),
                ('consent', models.CharField(max_length=5)),
                ('village_no', models.IntegerField(default=0)),
                ('gender', models.CharField(max_length=20)),
                ('age', models.IntegerField(default=0)),
                ('contact_client', models.CharField(max_length=20)),
                ('care_present', models.CharField(max_length=5)),
                ('contact_care', models.CharField(max_length=20)),
                ('photo', models.ImageField(default='images/default.png', upload_to='images/')),
                ('disability', models.CharField(max_length=250)),
                ('date', models.DateTimeField(auto_now_add=True)),
                ('health_risk', models.IntegerField(default=0)),
                ('health_require', models.TextField(blank=True)),
                ('health_goal', models.TextField(blank=True)),
                ('education_risk', models.IntegerField(default=0)),
                ('education_require', models.TextField(blank=True)),
                ('education_goal', models.TextField(blank=True)),
                ('social_risk', models.IntegerField(default=0)),
                ('social_require', models.TextField(blank=True)),
                ('social_goal', models.TextField(blank=True)),
                ('risk_score', models.IntegerField(default=0, editable=False)),
                ('last_modified', models.DateTimeField(auto_now=True)),
                ('is_new_client', models.BooleanField(blank=True, default=False)),
                ('goal_met_health_provision', models.TextField(blank=True, max_length=30)),
                ('goal_met_education_provision', models.TextField(blank=True, max_length=30)),
                ('goal_met_social_provision', models.TextField(blank=True, max_length=30)),
            ],
            options={
                'ordering': ['id'],
            },
        ),
        migrations.CreateModel(
            name='ClientHistoryRecord',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('date_created', models.DateTimeField(auto_now_add=True)),
                ('field', models.CharField(editable=False, max_length=100)),
                ('old_value', models.TextField(editable=False)),
                ('new_value', models.TextField(editable=False)),
                ('client', models.ForeignKey(editable=False, on_delete=django.db.models.deletion.CASCADE, to='clients.client')),
            ],
        ),
    ]
