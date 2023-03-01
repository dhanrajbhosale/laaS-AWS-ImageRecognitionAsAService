# app-tier
```
sudo nano /etc/systemd/system/my-app.service
```

```[Unit]
Description=My App Service
After=network.target

[Service]
User=ubuntu
WorkingDirectory=/home/ubuntu
ExecStart=/usr/bin/java -jar /home/ubuntu/apptier.jar
SuccessExitStatus=143

[Install]
WantedBy=multi-user.target
```

```
sudo systemctl daemon-reload
```

```
sudo systemctl enable my-app.service
```
