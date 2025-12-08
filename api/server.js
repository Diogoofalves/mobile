const express = require("express");
const cors = require("cors");
const multer = require("multer");
const fs = require("fs");
const path = require("path");
const { v4: uuid } = require("uuid");

const PORT = process.env.PORT || 3001;
const app = express();

const uploadDir = path.join(__dirname, "uploads");
fs.mkdirSync(uploadDir, { recursive: true });
const upload = multer({ dest: uploadDir });

app.use(cors());
app.use(express.json());
app.use("/uploads", express.static(uploadDir));

const nowIso = () => new Date().toISOString();
const daysAgo = (days) => new Date(Date.now() - days * 24 * 60 * 60 * 1000).toISOString();

let users = [
  {
    id: 1,
    fullName: "Dra. Ana Veterinaria",
    email: "vet@tpb.com",
    password: "123456",
    userType: "veterinario",
    phone: "+55 11 99999-0000",
    crmvNumber: "CRMV-SP 12345",
    createdAt: daysAgo(25),
  },
  {
    id: 2,
    fullName: "Carlos Trab.",
    email: "worker@tpb.com",
    password: "123456",
    userType: "funcionario",
    phone: "+55 11 98888-0000",
    createdAt: daysAgo(20),
  },
  {
    id: 3,
    fullName: "Mariana Produtora",
    email: "fazenda@tpb.com",
    password: "123456",
    userType: "fazendeiro",
    phone: "+55 11 97777-0000",
    createdAt: daysAgo(18),
  },
];

let farms = [
  {
    id: 1,
    name: "Fazenda Bela Vista",
    address: "Estrada Rural 123, SP",
    contactPhone: "+55 11 90000-0001",
    areaHectares: 320,
    cattleCount: 850,
    propertyType: "Corte",
    ownerId: 3,
  },
  {
    id: 2,
    name: "Sitio Bom Pastor",
    address: "Zona Rural 45, MG",
    contactPhone: "+55 11 90000-0002",
    areaHectares: 210,
    cattleCount: 430,
    propertyType: "Leite",
    ownerId: 3,
  },
];

let medicationRequests = [
  {
    id: 101,
    farmId: 1,
    veterinarianId: 1,
    assignedWorkerId: 2,
    animalIdentification: "Bovino 23",
    medicationName: "Ivomec",
    dosage: "50ml",
    applicationMethod: "Injetavel",
    scheduledDatetime: daysAgo(1),
    appliedDatetime: null,
    verifiedDatetime: null,
    priority: "alta",
    observations: "Aplicar na paleta",
    status: "pendente",
    applicationPhotoUrl: null,
    rejectionReason: null,
  },
  {
    id: 102,
    farmId: 1,
    veterinarianId: 1,
    assignedWorkerId: 2,
    animalIdentification: "Lote 12",
    medicationName: "Bravoxin 10",
    dosage: "20ml",
    applicationMethod: "Injetavel",
    scheduledDatetime: daysAgo(3),
    appliedDatetime: daysAgo(1),
    verifiedDatetime: null,
    priority: "media",
    observations: "Reforco do calendario",
    status: "aguardando_verificacao",
    applicationPhotoUrl: null,
    rejectionReason: null,
  },
  {
    id: 103,
    farmId: 2,
    veterinarianId: 1,
    assignedWorkerId: 2,
    animalIdentification: "Lote bezerros",
    medicationName: "Vytel-L",
    dosage: "10ml",
    applicationMethod: "Oral",
    scheduledDatetime: daysAgo(8),
    appliedDatetime: daysAgo(7),
    verifiedDatetime: daysAgo(6),
    priority: "baixa",
    observations: "Rehidratacao",
    status: "aprovado",
    applicationPhotoUrl: null,
    rejectionReason: null,
  },
  {
    id: 104,
    farmId: 2,
    veterinarianId: 1,
    assignedWorkerId: 2,
    animalIdentification: "Bovino 99",
    medicationName: "Antibiotico X",
    dosage: "30ml",
    applicationMethod: "Injetavel",
    scheduledDatetime: daysAgo(5),
    appliedDatetime: daysAgo(4.5),
    verifiedDatetime: daysAgo(4),
    priority: "alta",
    observations: "Animal com ferimento",
    status: "recusado",
    applicationPhotoUrl: null,
    rejectionReason: "Dose fora do protocolo",
  },
];

let activity = [
  {
    id: 1,
    type: "request",
    title: "Solicitacao criada",
    description: "Ivomec - Bovino 23",
    status: "pendente",
    timestamp: daysAgo(1.1),
    assignedTo: "Carlos Trab.",
  },
  {
    id: 2,
    type: "verification",
    title: "Aplicacao aguardando validacao",
    description: "Bravoxin 10 - Lote 12",
    status: "aguardando_verificacao",
    timestamp: daysAgo(0.8),
    veterinarian: "Dra. Ana",
  },
  {
    id: 3,
    type: "request",
    title: "Aplicacao aprovada",
    description: "Vytel-L - Lote bezerros",
    status: "aprovado",
    timestamp: daysAgo(6),
    veterinarian: "Dra. Ana",
  },
];

const tokens = new Map(); // token -> userId

const person = (user) =>
  user && {
    id: user.id,
    fullName: user.fullName,
    email: user.email,
    phone: user.phone || null,
  };

const publicUser = (user) => {
  const { password, ...rest } = user;
  return rest;
};

const addActivity = (entry) => {
  const record = { id: uuid(), timestamp: nowIso(), ...entry };
  activity = [record, ...activity].slice(0, 30);
  return record;
};

const issueToken = (userId) => {
  const token = `mock-token-${userId}-${uuid()}`;
  tokens.set(token, userId);
  return token;
};

const authMiddleware = (req, res, next) => {
  const header = req.headers.authorization;
  if (!header || !header.startsWith("Bearer ")) {
    return res.status(401).json({ message: "Token ausente" });
  }
  const token = header.replace("Bearer ", "");
  const userId = tokens.get(token);
  if (!userId) {
    return res.status(401).json({ message: "Token invalido" });
  }
  const user = users.find((u) => u.id === userId);
  if (!user) {
    return res.status(401).json({ message: "Usuario nao encontrado" });
  }
  req.user = user;
  req.token = token;
  next();
};

const computeRequestStats = (list) => {
  const now = new Date();
  const startOfDay = new Date(now);
  startOfDay.setHours(0, 0, 0, 0);
  const startOfWeek = new Date(startOfDay);
  startOfWeek.setDate(startOfWeek.getDate() - startOfWeek.getDay());
  const startOfMonth = new Date(startOfDay);
  startOfMonth.setDate(1);

  const pendingStatuses = ["pendente", "aguardando_aplicacao", "aguardando_verificacao"];
  const completedStatuses = ["aprovado"];

  const parseDate = (value) => (value ? new Date(value) : null);

  const completedList = list.filter((r) => completedStatuses.includes(r.status));
  const pendingList = list.filter((r) => pendingStatuses.includes(r.status));
  const overdue = pendingList.filter((r) => {
    const scheduled = parseDate(r.scheduledDatetime);
    return scheduled && scheduled < now;
  });

  const inRange = (dt, start) => {
    const parsed = parseDate(dt);
    return parsed && parsed >= start;
  };

  return {
    total: list.length,
    pending: pendingList.length,
    completed: completedList.length,
    overdue: overdue.length,
    todayCompleted: completedList.filter((r) => inRange(r.verifiedDatetime, startOfDay)).length,
    weekCompleted: completedList.filter((r) => inRange(r.verifiedDatetime, startOfWeek)).length,
    monthCompleted: completedList.filter((r) => inRange(r.verifiedDatetime, startOfMonth)).length,
  };
};

const attachRelations = (request) => {
  const farm = farms.find((f) => f.id === request.farmId);
  const veterinarian = users.find((u) => u.id === request.veterinarianId);
  const worker = users.find((u) => u.id === request.assignedWorkerId);

  return {
    ...request,
    farm: farm ? { id: farm.id, name: farm.name } : null,
    veterinarian: person(veterinarian),
    assignedWorker: person(worker),
  };
};

const refreshFarmStats = () => {
  farms = farms.map((farm) => {
    const farmRequests = medicationRequests.filter((r) => r.farmId === farm.id);
    const pending = farmRequests.filter((r) => !["aprovado", "recusado"].includes(r.status)).length;
    const completed = farmRequests.filter((r) => r.status === "aprovado").length;
    return { ...farm, requestsStats: { pending, completed } };
  });
};

const findRequest = (id) => medicationRequests.find((r) => r.id === id);

refreshFarmStats();

app.post("/api/auth/login", (req, res) => {
  const { email, password } = req.body || {};
  const user = users.find((u) => u.email === email && u.password === password);
  if (!user) {
    return res.status(401).json({ message: "Credenciais invalidas" });
  }
  const token = issueToken(user.id);
  res.json({ message: "ok", user: publicUser(user), token });
});

app.post("/api/auth/register", (req, res) => {
  const { email, password, fullName, userType = "funcionario", phone, specialization, crmvNumber, companyName, cnpj, mainAddress } =
    req.body || {};
  if (!email || !password || !fullName) {
    return res.status(400).json({ message: "Campos obrigatorios: email, password, fullName" });
  }
  const exists = users.some((u) => u.email === email);
  if (exists) {
    return res.status(400).json({ message: "Email ja cadastrado" });
  }

  const newUser = {
    id: users.length ? Math.max(...users.map((u) => u.id)) + 1 : 1,
    email,
    password,
    fullName,
    userType,
    phone: phone || null,
    specialization: specialization || null,
    crmvNumber: crmvNumber || null,
    companyName: companyName || null,
    cnpj: cnpj || null,
    mainAddress: mainAddress || null,
    createdAt: nowIso(),
  };
  users.push(newUser);
  const token = issueToken(newUser.id);
  res.status(201).json({ message: "Usuario criado", user: publicUser(newUser), token });
});

app.get("/api/profile", authMiddleware, (req, res) => {
  res.json(publicUser(req.user));
});

app.get("/api/dashboard", authMiddleware, (req, res) => {
  const stats = computeRequestStats(medicationRequests);
  const pendingVerifications = medicationRequests.filter((r) => r.status === "aguardando_verificacao").length;
  const recentVerified = medicationRequests.filter((r) => r.verifiedDatetime).length;

  const dashboardStats = {
    farms: { total: farms.length, active: farms.length },
    team: {
      veterinarians: users.filter((u) => u.userType === "veterinario").length,
      workers: users.filter((u) => u.userType === "funcionario").length,
      total: users.length,
    },
    requests: stats,
    verifications: { pending: pendingVerifications, todayVerified: recentVerified },
    tasks: computeRequestStats(medicationRequests.filter((r) => r.assignedWorkerId === req.user.id)),
  };

  res.json({
    userType: req.user.userType,
    stats: dashboardStats,
    lastUpdated: nowIso(),
  });
});

app.get("/api/dashboard/recent-activity", authMiddleware, (_req, res) => {
  res.json(activity);
});

app.get("/api/requests/pending-verification", authMiddleware, (_req, res) => {
  const pending = medicationRequests.filter((r) => r.status === "aguardando_verificacao").map(attachRelations);
  res.json(pending);
});

app.get("/api/requests", authMiddleware, (req, res) => {
  const farmId = req.query.farmId ? Number(req.query.farmId) : null;
  let list = medicationRequests;
  if (farmId) {
    list = list.filter((r) => r.farmId === farmId);
  }
  res.json(list.map(attachRelations));
});

app.get("/api/requests/mytasks", authMiddleware, (req, res) => {
  const tasks = medicationRequests.filter((r) => r.assignedWorkerId === req.user.id).map(attachRelations);
  res.json(tasks);
});

app.patch("/api/requests/:requestId/apply", authMiddleware, upload.single("photo"), (req, res) => {
  const requestId = Number(req.params.requestId);
  const request = findRequest(requestId);
  if (!request) {
    return res.status(404).json({ message: "Solicitacao nao encontrada" });
  }

  request.assignedWorkerId = request.assignedWorkerId || req.user.id;
  request.appliedDatetime = nowIso();
  request.status = "aguardando_verificacao";
  if (req.file) {
    request.applicationPhotoUrl = `/uploads/${req.file.filename}`;
  }
  addActivity({
    type: "request",
    title: "Aplicacao registrada",
    description: `${request.medicationName} - ${request.animalIdentification}`,
    status: request.status,
    assignedTo: req.user.fullName,
  });
  refreshFarmStats();

  res.json(attachRelations(request));
});

app.patch("/api/requests/:requestId/verify", authMiddleware, (req, res) => {
  if (req.user.userType !== "veterinario") {
    return res.status(403).json({ message: "Apenas veterinarios podem verificar aplicacoes" });
  }

  const requestId = Number(req.params.requestId);
  const request = findRequest(requestId);
  if (!request) {
    return res.status(404).json({ message: "Solicitacao nao encontrada" });
  }

  const { approved, rejectionReason } = req.body || {};
  request.status = approved ? "aprovado" : "recusado";
  request.rejectionReason = approved ? null : rejectionReason || "Sem justificativa";
  request.verifiedDatetime = nowIso();

  addActivity({
    type: "verification",
    title: `Aplicacao ${approved ? "aprovada" : "recusada"}`,
    description: `${request.medicationName} - ${request.animalIdentification}`,
    status: request.status,
    veterinarian: req.user.fullName,
  });
  refreshFarmStats();

  res.json(attachRelations(request));
});

app.get("/api/farms/my-farms", authMiddleware, (_req, res) => {
  refreshFarmStats();
  res.json(farms);
});

app.use((_req, res) => {
  res.status(404).json({ message: "Rota nao encontrada" });
});

app.listen(PORT, () => {
  console.log(`API mock do TPB Control rodando em http://localhost:${PORT}/api`);
});
