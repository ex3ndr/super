import { InvalidateSync } from "teslabot";
import { SuperClient } from "../api/client";
import { cleanAndReload } from "../reload/cleanAndReload";
import { AppState } from "react-native";
import { log } from "../../utils/logs";

export class TokenExpireService {
    readonly client: SuperClient;
    #sync: InvalidateSync;

    constructor(client: SuperClient) {
        this.client = client;
        this.#sync = new InvalidateSync(async () => {
            if (!await this.client.tokenAndAccountStatus()) {
                log('EXP', 'Token expired. Reloading...');
                await cleanAndReload();
            }
        });
        this.#sync.invalidate();
        AppState.addEventListener('change', () => {
            this.#sync.invalidate();
        });
    }
}