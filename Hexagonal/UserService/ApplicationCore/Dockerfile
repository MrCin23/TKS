#FROM mongo:7.0.2
#COPY --chown=999 --chmod=600 keyFileB /etc/mongo/keyFileB
#RUN chmod 400 /etc/mongo/keyFileB
#COPY --chown=999 --chmod=600 mongod.conf /etc/mongod.conf

FROM mongo:8.0.1

COPY keyFileB /etc/mongo/keyFileB
RUN chown 999 /etc/mongo/keyFileB && chmod 600 /etc/mongo/keyFileB

COPY mongod.conf /etc/mongod.conf
RUN chown 999 /etc/mongod.conf && chmod 600 /etc/mongod.conf
