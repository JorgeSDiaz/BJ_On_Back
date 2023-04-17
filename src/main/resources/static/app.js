const app = (() => {
    let apiClient = client;
    let stompClient = null;

    let player = "";
    let boxes = {};
    let colorToken = 'red';

    const Token = (colorToken, xPosition, yPosition) => {
        return {
            color: colorToken,
            x: xPosition,
            y: yPosition
        };
    };

    const Box = (idBox, canvasBox, ctx, tenDollarsTokensArray, fiftyDollarsTokensArray,
                 oneHundredDollarsTokensArray, fiveHundredDollarsTokensArray) => {
        return {
            id: idBox,
            canvas: canvasBox,
            context: ctx,
            tenDollarsTokens: tenDollarsTokensArray,
            fiftyDollarsTokens: fiftyDollarsTokensArray,
            oneHundredDollarsTokens: oneHundredDollarsTokensArray,
            fiveHundredDollarsTokens: fiveHundredDollarsTokensArray
        };
    }

    const setTokenSelected = (color) => {
        colorToken = color;
        console.log(colorToken);
    }

    const drawToken = (ctx, token) => {
        ctx.fillStyle = token.color;
        ctx.beginPath();
        ctx.arc(token.x, token.y , 5, 0, 2 * Math.PI);
        ctx.fill();
    };

    const createToken = (box, x, y) => {
        let tokenArray = [];
        let color = "";

        switch (colorToken) {
            case 'red':
                tokenArray = box.tenDollarsTokens;
                color = 'red';
                break;
            case 'yellow':
                tokenArray = box.fiftyDollarsTokens;
                color = 'yellow';
                break;
            case 'blue':
                tokenArray = box.oneHundredDollarsTokens;
                color = 'blue';
                break;
            case 'green':
                tokenArray = box.fiveHundredDollarsTokens;
                color = 'green';
                break;
            default:
                tokenArray = box.tenDollarsTokens;
                color = 'red';
                break;
        }

        let newToken = Token(color, x, y);
        client.registerBet(box.id, player, newToken).then(() => {
            tokenArray.push(newToken);
            drawToken(box.context, tokenArray[tokenArray.length - 1]);
            reduceTokens(box, x, y);
        }).catch((err) => {
            alert(JSON.parse(err.responseText).message);
        })
    }

    const reduceTokens = (box, x, y) => {
        let arrays = {
            ten: box.tenDollarsTokens,
            fifty: box.fiftyDollarsTokens,
            oneHundred: box.oneHundredDollarsTokens,
            fiveHundred: box.fiveHundredDollarsTokens
        };

        if (box.tenDollarsTokens.length >= 5) {
            box.fiftyDollarsTokens.push(Token('yellow', x, y));
            box.tenDollarsTokens.length = 0;
            delete arrays.ten;
        }

        if (box.fiftyDollarsTokens.length >= 2) {
            box.oneHundredDollarsTokens.push(Token('blue', x, y));
            box.fiftyDollarsTokens.length = 0;
            delete arrays.fifty;
        }

        if (box.oneHundredDollarsTokens.length >= 5) {
            box.fiveHundredDollarsTokens.push(Token('green', x, y));
            box.oneHundredDollarsTokens.length = 0;
            delete arrays.oneHundred;
        }

        if (Object.keys(arrays).length >= 1) {
            box.context.clearRect(0, 0, box.canvas.width, box.canvas.height);

            Object.keys(arrays).forEach((key) => {
                arrays[key].forEach((token) => {
                    drawToken(box.context, token);
                })
            })
        }

        arrays.length = 0;
    }

    const addToken = (box, token) => {
        if (token.color === 'red') {
            box.tenDollarsTokens.push(token);
        } else if (token.color === 'yellow') {
            box.fiftyDollarsTokens.push(token);
        } else if (token.color === 'blue') {
            box.oneHundredDollarsTokens.push(token);
        } else if (token.color === 'green') {
            box.fiveHundredDollarsTokens.push(token);
        } else {
            box.tenDollarsTokens.push(token);
        }

        drawToken(box.context, token);
        reduceTokens(box, token.x, token.y);
    }

    const initDraw = () => {
        for (let number = 1; number < 8; number++) {
            let canvas = document.getElementById("canvas" + number);

            boxes[number.toString()] = Box(22 - number, canvas, canvas.getContext("2d"), [],
                [], [], []);
        }

        Object.keys(boxes).forEach((key) => {
            let box = boxes[key];
            box.canvas.addEventListener('click', (event) => {
                const rect = box.canvas.getBoundingClientRect();
                const x = event.clientX - rect.left;
                const y = event.clientY - rect.top;

                createToken(box, x, y);

                console.log("Id: " + box.id + ", $10: " + box.tenDollarsTokens.length + ", " +
                    "$50: " + box.fiftyDollarsTokens.length + ", $100: " + box.oneHundredDollarsTokens.length +
                    ", $500: " + box.fiveHundredDollarsTokens.length);
            })

            Object.keys(boxes).forEach((key) => {
                let box = boxes[key];
                box.context.canvas.width = box.canvas.offsetWidth;
                box.context.canvas.height = box.canvas.offsetHeight;
            });
        })
    };

    const connect = () => {
        initDraw();
        console.info('Connect to Ws...');
        let socket = new SockJS('/BJgame');
        stompClient = Stomp.over(socket);
        stompClient.connect({}, (frame) => {
            console.log('Connected:' + frame);
            stompClient.subscribe("/topic/registerbet", (eventBody) => {
                let res = JSON.parse(eventBody.body);

                let token = res.token;
                let box = boxes[22 - res.id];
                console.log(box);
                console.log(token);
                addToken(box, token);
            });
        });
    }

    const table = () => {
        const tokens = [];
        const canvasTabla = document.getElementById("canvasTabla");
        const ctxTabla = canvasTabla.getContext("2d");
        const tablaImg = new Image();

        tablaImg.onload = function() {
            ctxTabla.drawImage(tablaImg, 0, 0, canvasTabla.width, canvasTabla.height);
        };

        tablaImg.src = "src/tabla2.png";
    }

    return {
        init: () => {
            table();
            player = new URLSearchParams(window.location.search).get('user');
            initDraw();
            connect();
            console.log(player);
        },
        tokenSelected: (color) => {

            setTokenSelected(color);
        }
    };
})();