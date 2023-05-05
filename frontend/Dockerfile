FROM node:20-alpine
ARG BASE_URL
WORKDIR /app
COPY package*.json .
RUN npm i --silent
COPY . .
RUN echo "API_BASE_URL=${BASE_URL}" > .env
EXPOSE 5173
CMD ["npm", "run",  "dev"]