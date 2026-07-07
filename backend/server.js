import express from 'express';
import cors from 'cors';
import dotenv from 'dotenv';
import bcrypt from 'bcryptjs';
import User from './models/User.js';

import authRoutes from './routes/auth.js';
import adminRoutes from './routes/admin.js';
import profileRoutes from './routes/profile.js';

dotenv.config();

const app = express();
const PORT = process.env.PORT || 8080;

// Middleware
app.use(cors());
app.use(express.json());
app.use(express.urlencoded({ extended: true }));

// Seed default admin if none exists
const seedAdmin = async () => {
  const adminExists = User.findOne({ role: 'ADMIN' });
  if (!adminExists) {
    const salt = await bcrypt.genSalt(10);
    const hashedPassword = await bcrypt.hash('admin123', salt);
    User.create({
      username: 'admin',
      email: 'admin@system.com',
      password: hashedPassword,
      fullName: 'System Admin',
      role: 'ADMIN'
    });
    console.log('👑 Default Admin created → Email: admin@system.com | Password: admin123');
  }
};

seedAdmin();

// Routes
app.use('/api/auth', authRoutes);
app.use('/api/admin', adminRoutes);
app.use('/api', profileRoutes);

// Base route
app.get('/', (req, res) => {
  res.send('✅ Node.js Backend is running!');
});

// Global Error Handler
app.use((err, req, res, next) => {
  console.error(err.stack);
  res.status(500).json({ success: false, message: 'Internal Server Error' });
});

app.listen(PORT, () => {
  console.log(`🚀 Server running on http://localhost:${PORT}`);
});
