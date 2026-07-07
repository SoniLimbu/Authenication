import fs from 'fs';
import path from 'path';
import { fileURLToPath } from 'url';
import { v4 as uuidv4 } from 'uuid';

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

const DB_PATH = path.join(__dirname, '../data/db.json');

// Ensure data directory and file exist
const ensureDB = () => {
  const dir = path.join(__dirname, '../data');
  if (!fs.existsSync(dir)) fs.mkdirSync(dir, { recursive: true });
  if (!fs.existsSync(DB_PATH)) {
    fs.writeFileSync(DB_PATH, JSON.stringify({ users: [] }, null, 2));
  }
};

const readDB = () => {
  ensureDB();
  return JSON.parse(fs.readFileSync(DB_PATH, 'utf-8'));
};

const writeDB = (data) => {
  ensureDB();
  fs.writeFileSync(DB_PATH, JSON.stringify(data, null, 2));
};

// Simulate Mongoose-like model
const UserDB = {
  findOne: (query) => {
    const db = readDB();
    const keys = Object.keys(query);
    
    // Handle $or query
    if (query.$or) {
      return db.users.find(user =>
        query.$or.some(condition =>
          Object.entries(condition).every(([k, v]) => user[k] === v)
        )
      ) || null;
    }
    
    return db.users.find(user => keys.every(k => user[k] === query[k])) || null;
  },

  find: (query = {}) => {
    const db = readDB();
    if (Object.keys(query).length === 0) return db.users;
    const keys = Object.keys(query);
    return db.users.filter(user => keys.every(k => user[k] === query[k]));
  },

  findById: (id) => {
    const db = readDB();
    return db.users.find(u => u.id === id) || null;
  },

  countDocuments: (query = {}) => {
    const db = readDB();
    if (Object.keys(query).length === 0) return db.users.length;
    const keys = Object.keys(query);
    return db.users.filter(user => keys.every(k => user[k] === query[k])).length;
  },

  create: (data) => {
    const db = readDB();
    const newUser = {
      id: uuidv4(),
      username: data.username || '',
      email: data.email || '',
      password: data.password || '',
      fullName: data.fullName || '',
      role: data.role || 'CUSTOMER',
      enabled: data.enabled !== undefined ? data.enabled : true,
      createdAt: new Date().toISOString()
    };
    db.users.push(newUser);
    writeDB(db);
    // Return without password
    const { password, ...safeUser } = newUser;
    return safeUser;
  },

  findByIdAndUpdate: (id, updates) => {
    const db = readDB();
    const idx = db.users.findIndex(u => u.id === id);
    if (idx === -1) return null;
    db.users[idx] = { ...db.users[idx], ...updates };
    writeDB(db);
    const { password, ...safeUser } = db.users[idx];
    return safeUser;
  },

  findByIdAndDelete: (id) => {
    const db = readDB();
    const idx = db.users.findIndex(u => u.id === id);
    if (idx === -1) return null;
    const deleted = db.users.splice(idx, 1)[0];
    writeDB(db);
    return deleted;
  },

  // Used for save() method on found user objects
  updateUser: (id, updates) => {
    const db = readDB();
    const idx = db.users.findIndex(u => u.id === id);
    if (idx === -1) return null;
    db.users[idx] = { ...db.users[idx], ...updates };
    writeDB(db);
    return db.users[idx];
  },

  getRawUser: (id) => {
    const db = readDB();
    return db.users.find(u => u.id === id) || null;
  }
};

export default UserDB;
