import express from 'express';
import { verifyToken, requireRole } from '../middleware/auth.js';

const router = express.Router();

// @route   GET /api/staff/profile
router.get('/staff/profile', verifyToken, requireRole(['STAFF', 'ADMIN']), (req, res) => {
  try {
    res.json({
      success: true,
      message: 'Staff profile fetched successfully',
      data: req.user
    });
  } catch (error) {
    console.error(error);
    res.status(500).json({ success: false, message: 'Server error' });
  }
});

// @route   GET /api/customer/profile
router.get('/customer/profile', verifyToken, requireRole(['CUSTOMER', 'STAFF', 'ADMIN']), (req, res) => {
  try {
    res.json({
      success: true,
      message: 'Customer profile fetched successfully',
      data: req.user
    });
  } catch (error) {
    console.error(error);
    res.status(500).json({ success: false, message: 'Server error' });
  }
});

export default router;
