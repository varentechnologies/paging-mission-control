/* 
*  The following code was developed and tested under node.js
*  How to run:
*  1. Save the data in a text file such as "input.txt"
*  2. Run node.js to read and process the file using the following command:
*     node ./lanli_code.js input.txt
*/

// Make sure we got a filename on the command line.
if (process.argv.length < 3) {
  console.log('Usage: node ' + process.argv[1] + ' FILENAME');
  process.exit(1);
}
// Read the file line by line and process and print the result in the end
var fs = require('fs')
  , filename = process.argv[2];

const readline = require('readline'); 

const file = readline.createInterface({ 
	input: fs.createReadStream(filename), 
	output: process.stdout, 
	terminal: false
}); 

const result = [],
      tQ = {},
      vQ = {};

file.on('line', (line) => { 
    const data = line.split("|");
    const ts = data[0];
    const id = data[1];
    const val = 1*data[6];
    const redH = 1*data[2];
    const redL = 1*data[5];
    //const yeH = 1*data[3];
    //const yeL = 1*data[4];
    const type = data[7]; //TSTAT, BATT

    //TSTAT red high
    if(type=="TSTAT" && val>redH){
        if(!tQ[id])tQ[id]=[];
        tQ[id].push(ts);
    }

    //BATT red low
    if(type=="BATT" && val<redL){
        if(!vQ[id])vQ[id]=[];
        vQ[id].push(ts);
    }
});

file.input.on("end", ()=>{
    for(const id in tQ){
        const v = tQ[id];
        for(let i=0, len=v.length; i<len-2; i++) {
            const dt = strToDate(v[i]);
            const dt_3rd = strToDate(v[i+2]);
            const dif = dt_3rd.getTime()-dt.getTime();

            //3 records within 5 min (300s)
            if(dif<300000){
                result.push({
                        satelliteId: id,
                        severity: "RED HIGH",
                        component: "TSTAT",
                        timestamp: dt.toISOString()
                });
            }
        }
    };

    for(const id in vQ){
        const v = vQ[id];
        for(let i=0, len=v.length; i<len-2; i++) {
            const dt = strToDate(v[i]);
            const dt_3rd = strToDate(v[i+2]);
            const dif = dt_3rd.getTime()-dt.getTime();

            //3 records within 5 min (300s)
            if(dif<300000){
                result.push({
                        satelliteId: id,
                        severity: "RED LOW",
                        component: "BATT",
                        timestamp: dt.toISOString()
                });
            }
        }
    };

    console.log("the following is result:");
    console.log(result);
});

function strToDate(dateString){
    //const dateString  = "20180101 23:01:38.001";
    const year        = dateString.substring(0,4);
    const month       = 1*dateString.substring(4,6)-1;
    const day         = dateString.substring(6,8);
    const hr = dateString.substring(9,11);
    const min = dateString.substring(12,14);
    const sec = dateString.substring(15,17);
    const ms = dateString.substring(18,21);
    const date = new Date(Date.UTC(year,month,day, hr, min, sec, ms));
    
    return date;
}
