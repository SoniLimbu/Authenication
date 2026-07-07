import express from 'express';
import bcrypt from 'bcryptjs';
import User from '../models/User.js';
import { verifyToken, requireRole } from '../middleware/auth.js';

const router = express.Router();

// All admin routes require ADMIN role
router.use(verifyToken, requireRole(['ADMIN']));

// @route   GET /api/admin/stats
router.get('/stats', (req, res) => {
  try {
    const totalUsers = User.countDocuments();
    const adminCount = User.countDocuments({ role: 'ADMIN' });
    const staffCount = User.countDocuments({ role: 'STAFF' });
    const customerCount = User.countDocuments({ role: 'CUSTOMER' });

    res.json({
      success: true,
      message: 'Stats fetched successfully',
      data: { totalUsers, adminCount, staffCount, customerCount }
    });
  } catch (error) {
    console.error(error);
    res.status(500).json({ success: false, message: 'Server error' });
  }
});

// @route   GET /api/admin/users
router.get('/users', (req, res) => {
  try {
    const users = User.find();
    res.json({
      success: true,
      message: 'Users fetched successfully',
      data: users
    });
  } catch (error) {
    console.error(error);
    res.status(500).json({ success: false, message: 'Server error' });
  }
});

// @route   POST /api/admin/staff
router.post('/staff', async (req, res) => {
  try {
    const { username, email, password, confirmPassword } = req.body;

    if (!username || !email || !password || !confirmPassword) {
      return res.status(400).json({ success: false, message: 'Please fill in all fields' });
    }
    if (password !== confirmPassword) {
      return res.status(400).json({ success: false, message: 'Passwords do not match' });
    }

    const existingUser = User.findOne({ $or: [{ email }, { username }] });
    if (existingUser) {
      return res.status(400).json({ success: false, message: 'Email or username already exists' });
    }

    const salt = await bcrypt.genSalt(10);
    const hashedPassword = await bcrypt.hash(password, salt);

    const newUser = User.create({
      email,
      username,
      password: hashedPassword,
      role: 'STAFF'
    });

    res.status(201).json({
      success: true,
      message: 'Staff user created successfully',
      data: newUser
    });
  } catch (error) {
    console.error(error);
    res.status(500).json({ success: false, message: 'Server error' });
  }
});

// @route   PUT /api/admin/users/:id/role
router.put('/users/:id/role', (req, res) => {
  try {
    const { role } = req.body;
    const user = User.findByIdAndUpdate(req.params.id, { role });
    if (!user) return res.status(404).json({ success: false, message: 'User not found' });
    res.json({ success: true, message: 'User role updated', data: user });
  } catch (error) {
    console.error(error);
    res.status(500).json({ success: false, message: 'Server error' });
  }
});

// @route   PUT /api/admin/users/:id/toggle-status
router.put('/users/:id/toggle-status', (req, res) => {
  try {
    const rawUser = User.getRawUser(req.params.id);
    if (!rawUser) return res.status(404).json({ success: false, message: 'User not found' });

    const updated = User.findByIdAndUpdate(req.params.id, { enabled: !rawUser.enabled });
    res.json({ success: true, message: 'User status toggled', data: updated });
  } catch (error) {
    console.error(error);
    res.status(500).json({ success: false, message: 'Server error' });
  }
});

// @route   DELETE /api/admin/users/:id
router.delete('/users/:id', (req, res) => {
  try {
    const deleted = User.findByIdAndDelete(req.params.id);
    if (!deleted) return res.status(404).json({ success: false, message: 'User not found' });
    res.json({ success: true, message: 'User deleted successfully' });
  } catch (error) {
    console.error(error);
    res.status(500).json({ success: false, message: 'Server error' });
  }
});

export default router;
