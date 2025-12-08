# TPB Control Mobile

Aplicativo Android (Jetpack Compose) com uma API mock inclusa para rodar o app sem depender de outros projetos.

## Como rodar a API local

1. `cd tpb-control-android/api`
2. `npm install`
3. `npm run dev` (ou `npm start` para modo simples)
4. A API fica em `http://localhost:3001/api` (o app Android usa `10.0.2.2:3001` no emulador).

Credenciais de exemplo:
- Veterinário: `vet@tpb.com` / `123456`
- Funcionário: `worker@tpb.com` / `123456`
- Produtor: `fazenda@tpb.com` / `123456`

## Base URL do app

O app aponta por padrão para `http://10.0.2.2:3001/api/` em `app/src/main/java/com/tpbcontrol/mobile/data/remote/NetworkModule.kt`.
- Emulador Android: use `10.0.2.2` (já configurado).
- Dispositivo físico: troque para o IP da sua máquina na mesma rede, por exemplo `http://192.168.0.10:3001/api/`.

## Endpoints implementados na mock

- `POST /auth/login`, `POST /auth/register`
- `GET /profile`
- `GET /dashboard`, `GET /dashboard/recent-activity`
- `GET /requests`, `GET /requests/mytasks`, `GET /requests/pending-verification`
- `PATCH /requests/:id/apply` (multipart com foto opcional)
- `PATCH /requests/:id/verify`
- `GET /farms/my-farms`

Todos respondem usando dados em memória, sem banco de dados.

## Rodar o app

Abra `tpb-control-android` no Android Studio, sincronize o Gradle e execute no emulador. Deixe a API acima rodando antes de abrir o app.
