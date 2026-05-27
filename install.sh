#!/bin/bash
set -e

N=24  # Змініть на свій номер залікової книжки

echo "=== Updating system ==="
apt-get update -y && apt-get upgrade -y

echo "=== Installing packages ==="
apt-get install -y mariadb-server nginx openjdk-11-jdk maven git sudo

echo "=== Creating users ==="
useradd -r -s /bin/false app || true

for user in student teacher operator; do
    useradd -m -s /bin/bash $user || true
    echo "$user:12345678" | chpasswd
    chage -d 0 $user
done

usermod -aG sudo student
usermod -aG sudo teacher

echo "operator ALL=(ALL) NOPASSWD: /bin/systemctl start mywebapp, /bin/systemctl stop mywebapp, /bin/systemctl restart mywebapp, /bin/systemctl status mywebapp, /bin/systemctl reload nginx" > /etc/sudoers.d/operator

echo "=== Setting up MariaDB ==="
systemctl start mariadb
mysql -e "CREATE DATABASE IF NOT EXISTS mywebapp;"
mysql -e "CREATE USER IF NOT EXISTS 'mywebapp_user'@'localhost' IDENTIFIED BY 'mywebapp_pass';"
mysql -e "GRANT ALL PRIVILEGES ON mywebapp.* TO 'mywebapp_user'@'localhost';"
mysql -e "FLUSH PRIVILEGES;"

echo "=== Building Java application ==="
mkdir -p /opt/mywebapp
cp -r * /opt/mywebapp/
cd /opt/mywebapp
mvn clean package
cp target/mywebapp-1.0.0.jar /opt/mywebapp/mywebapp.jar

echo "=== Setting up systemd service ==="
cp mywebapp.service /etc/systemd/system/
cp mywebapp.socket /etc/systemd/system/
systemctl daemon-reload
systemctl enable mywebapp.socket
systemctl enable mywebapp.service
systemctl start mywebapp

echo "=== Configuring Nginx ==="
cp nginx.conf /etc/nginx/sites-available/mywebapp
ln -sf /etc/nginx/sites-available/mywebapp /etc/nginx/sites-enabled/
rm -f /etc/nginx/sites-enabled/default
nginx -t && systemctl restart nginx

echo "=== Creating gradebook ==="
echo $N > /home/student/gradebook
chown student:student /home/student/gradebook
chmod 644 /home/student/gradebook

echo "=== Disabling default user ==="
passwd -l root
userdel -r ubuntu 2>/dev/null || true

echo "=== Installation complete ==="
echo "You can now access the web app at http://<VM_IP>/"
