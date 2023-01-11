# Energy Utility Platform

## Frontend

This is the frontend of the Energy Utility Platform.

## Available Scripts

In the project directory, you can run:

### `npm install`
Installs all the dependencies needed for the project.

### `npm start`

Runs the app in the development mode.\
Open [http://localhost:3000](http://localhost:3000) to view it in your browser.

##Also see the nginx.conf file

## Docker Deployment
The project can be deployed locally by running the command `docker-compose up` in the root directory of the project.\
The frontend will be available at [http://localhost:9090](http://localhost:9090).


## Actions needed in order to run & deploy the frontend
COPY package.json package-lock.json ./
RUN npm install

# Copy the main application
COPY . ./

# Arguments
ARG REACT_APP_API_BASE_URL
ENV REACT_APP_API_BASE_URL=${REACT_APP_API_BASE_URL}

# Build the application
RUN npm run build

#### Stage 2: Serve the React application from Nginx
FROM nginx:1.17.0-alpine

# Copy the react build from Stage 1
COPY --from=build /app/build /var/www

# Copy our custom nginx config
COPY nginx.conf /etc/nginx/nginx.conf

# Expose port 3000 to the Docker host, so we can access it from the outside.
EXPOSE 80

ENTRYPOINT ["nginx","-g","daemon off;"]