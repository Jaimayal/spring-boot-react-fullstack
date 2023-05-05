FROM node:20-alpine
WORKDIR /app
COPY package*.json .
RUN npm i --silent
COPY . .
RUN echo "API_BASE_URL=http://localhost:8081" > .env
EXPOSE 5173
CMD ["npm", "run",  "dev"]