client = (() => {
    // Private
    const resource = "http://localhost:8080/v1/game";
    const registerBet = (id, owner, token) => {
        return new Promise( (resolve, reject) => {
            $.ajax(
                {
                    url: resource + "/betBox/" + id + "/owner/" + owner,
                    type: "POST",
                    data: JSON.stringify(token),
                    contentType: "application/json",
                    success: (data) => {
                        resolve(data);
                    },
                    error: (err) => {
                        reject(err);
                    }
                }
            )
            }
        );
    };

    // Public
    return {
        registerBet: (id, owner, token) => {
            return registerBet(id, owner, token);
        }
    };
})();