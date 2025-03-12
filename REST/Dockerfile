#FROM mongo:7.0.2
#COPY --chown=999 --chmod=600 keyFile /etc/mongo/keyFile
#RUN chmod 400 /etc/mongo/keyFile
#COPY --chown=999 --chmod=600 mongod.conf /etc/mongod.conf

FROM mongo:8.0.1

COPY keyFile /etc/mongo/keyFile
RUN chown 999 /etc/mongo/keyFile && chmod 600 /etc/mongo/keyFile

COPY mongod.conf /etc/mongod.conf
RUN chown 999 /etc/mongod.conf && chmod 600 /etc/mongod.conf
