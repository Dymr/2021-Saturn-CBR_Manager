# How to setup Local Development

## Django

1. Run your server at 0.0.0.0 on port 8000

   `python manage.py runserver 0.0.0.0:8000`

   

2. Get your local computer IP 

   - Open a terminal
   - Run `ipconfig` might be different for mac `man ipconfig`?
   - Look for an `IPV4 Address` not `Default Gateway`
   - Use this address to access Django and copy this somewhere. We will need this for the Android section.

# Android

1. Head to `frontend/` and find `local.properties`

2. Add a line to the file, and place your IP you found earlier in the appropriate field

   `API_URL="http://<YOUR_IP>:8000/"`

   - quotes are important here
   - the last slash is important

3. Your file should look like this now

   ```
   sdk.dir=C\:\\Users\\tangj\\AppData\\Local\\Android\\Sdk
   API_URL="http://<YOUR_IP>:8000/" 
   ```



# Done!

Now you should be able to connect to the Django API with Android.

