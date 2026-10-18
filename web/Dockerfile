FROM dhi.io/node:26.12.0-alpine3.24

WORKDIR /app

ENV NODE_ENV=production
ENV PORT=3000
ENV HOSTNAME="0.0.0.0"

RUN mkdir .next
RUN chown node:node .next

COPY --chown=node:node /.next/standalone ./
COPY --chown=node:node /.next/static ./.next/static

USER node

EXPOSE 3000

CMD ["node", "server.js"]