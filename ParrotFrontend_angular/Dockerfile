# Use official Node.js 24 image
FROM node:24

# Set working directory
WORKDIR /app

# Copy package files
COPY package*.json ./

# Install dependencies
RUN npm install

# Copy the rest of the application
COPY . .

# Expose Angular dev server port
EXPOSE 4200

# Start Angular development server
CMD ["npm", "start", "--", "--host", "0.0.0.0", "--poll", "2000"]