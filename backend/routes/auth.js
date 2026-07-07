import express from 'express';
import bcrypt from 'bcryptjs';
import jwt from 'jsonwebtoken';
import User from '../models/User.js';
import dotenv from 'dotenv';

dotenv.config();

const router = express.Router();

// @route   POST /api/auth/register
router.post('/register', async (req, res) => {
  try {
    const { email, password, confirmPassword } = req.body;

    if (!email || !password || !confirmPassword) {
      return res.status(400).json({ success: false, message: 'Please fill in all fields' });
    }
    if (password !== confirmPassword) {
      return res.status(400).json({ success: false, message: 'Passwords do not match' });
    }
    if (password.length < 6) {
      return res.status(400).json({ success: false, message: 'Password must be at least 6 characters' });
    }

    const existingUser = User.findOne({ email });
    if (existingUser) {
      return res.status(400).json({ success: false, message: 'Email is already registered' });
    }

    const salt = await bcrypt.genSalt(10);
    const hashedPassword = await bcrypt.hash(password, salt);
    const username = email.split('@')[0] + Math.floor(Math.random() * 1000);

    const newUser = User.create({
      email,
      password: hashedPassword,
      username,
      role: 'CUSTOMER'
    });

    res.status(201).json({
      success: true,
      message: 'Registration successful! You can now log in.',
      data: newUser
    });
  } catch (error) {
    console.error(error);
    res.status(500).json({ success: false, message: 'Server error' });
  }
});

// @route   POST /api/auth/login
router.post('/login', async (req, res) => {
  try {
    const { email, password } = req.body;

    if (!email || !password) {
      return res.status(400).json({ success: false, message: 'Please provide email and password' });
    }

    const rawUser = User.findOne({ email });
    if (!rawUser) {
      return res.status(401).json({ success: false, message: 'Invalid email or password' });
    }
    if (!rawUser.enabled) {
      return res.status(403).json({ success: false, message: 'User account is disabled' });
    }

    const isMatch = await bcrypt.compare(password, rawUser.password);
    if (!isMatch) {
      return res.status(401).json({ success: false, message: 'Invalid email or password' });
    }

    const payload = {
      id: rawUser.id,
      email: rawUser.email,
      username: rawUser.username,
      fullName: rawUser.fullName,
      role: rawUser.role
    };

    const token = jwt.sign(payload, process.env.JWT_SECRET, { expiresIn: '1d' });

    res.json({
      success: true,
      message: 'Login successful',
      data: { token, ...payload }
    });
  } catch (error) {
    console.error(error);
    res.status(500).json({ success: false, message: 'Server error' });
  }
});

export default router;
