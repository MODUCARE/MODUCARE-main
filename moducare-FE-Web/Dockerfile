FROM node:20.15 as build

WORKDIR /moducare

COPY package.json package-lock.json ./
RUN npm install

COPY . .
RUN npm run build

FROM nginx:latest

COPY conf/default.conf /etc/nginx/conf/default.conf

COPY --from=build /moducare/build /usr/share/nginx/html

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
