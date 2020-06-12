const defaultURL = process.env.VUE_APP_API_URL;
const latestPath = "/current";
const nextUpdatePath = "nextupdate";
const historyPath = "/history";
const historyDaysParam = "?days=";

export async function getLatestTemp(){
    const url = defaultURL + latestPath;

    let response = await fetch(
        url
    );
    
    return response;
}

export async function getNextUpdateTime(){
    const url = defaultURL + nextUpdatePath;

    let response = await fetch(
        url
    );
    
    return response;
}

export async function getHistory(days){
    let url = defaultURL + historyPath;
    if(days !== undefined){
        url = url + historyDaysParam + days;
    }

    let response = await fetch(
        url
    );
    
    return response;
}