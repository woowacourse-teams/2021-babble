FROM nginx

COPY nginx.conf /etc/nginx/nginx.conf 
COPY api-fullchain.pem /etc/letsencrypt/live/api.babble.gg/fullchain.pem
COPY api-privkey.pem /etc/letsencrypt/live/api.babble.gg/privkey.pem
COPY sonarqube-fullchain.pem /etc/letsencrypt/live/sonarqube.babble.gg/fullchain.pem
COPY sonarqube-privkey.pem /etc/letsencrypt/live/sonarqube.babble.gg/privkey.pem

