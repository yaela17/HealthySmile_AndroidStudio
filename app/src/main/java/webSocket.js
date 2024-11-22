// server.js
const express = require('express');
const http = require('http');
const socketIo = require('socket.io');

const app = express();
const server = http.createServer(app);
const io = socketIo(server);

// Cuando un cliente se conecta
io.on('connection', (socket) => {
    console.log('Usuario conectado');

    // Escuchar el mensaje del cliente
    socket.on('enviarMensaje', (mensaje) => {
        console.log('Mensaje recibido:', mensaje);

        // Enviar el mensaje a todos los usuarios conectados
        io.emit('recibirMensaje', mensaje);
    });

    // Cuando el cliente se desconecta
    socket.on('disconnect', () => {
        console.log('Usuario desconectado');
    });
});

// Arrancar el servidor en el puerto 3000
server.listen(3000, () => {
    console.log('Servidor WebSocket corriendo en http://localhost:3000');
});
