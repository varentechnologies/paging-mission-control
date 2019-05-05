const express = require('express')
const readLine = require('readline')
const fs = require('fs')
const path = require('path')
const bodyParser = require('body-parser')
const multer = require('multer')
const upload = multer({ dest: 'uploads/' })

const app = express()

app.set('view engine', 'ejs')
app.use(express.static('public'))

const PORT = process.env.PORT || 5000

const readFile = async file => {
  let rl = readLine.createInterface( { input: fs.createReadStream(file) })
  const lineReader = () => {
    return new Promise((resolve, reject) => {
      const satellites = {}
      rl.on('line', line => {
        const firstCharacter = line.slice(line.indexOf(' ') + 1, line.length)
        const data = firstCharacter.split('|')
        let severity = 
          calculateSeverity(
            data[7], 
            parseInt(data[2]), 
            parseInt(data[5]), 
            parseInt(data[6])
          )
        if(!satellites[data[1]]) satellites[data[1]] = 
          {
            id: data[1], 
            component: data[7], 
            timestamp: data[0], 
            BATT: 0, 
            TSTAT: 0
          }
        if(severity) satellites[data[1]][data[7]]++
        resolve(satellites)
      })
    })
  }
 return lineReader().then(res => {
  let warnings = []
  const newArray = Object.values(res)
  for(let val of newArray) {
    if(val.BATT >= 3) {
      warnings.push(new Warning(val.timestamp, val.id, 'RED LOW', val.component))
    }
    if(val.TSTAT >=3) {
      warnings.push(new Warning(val.timestamp, val.id, 'RED HIGH', val.component))
    }
  }
  return warnings
 }).then(res => {
   console.log('Warning!!!', res)
   return (res)
 })
}

app.use(bodyParser.urlencoded({ extended: true }))

app.get('/', (req, res) => {
  res.render('index', { data: []})
})

app.post('/evaluate', upload.single('satellite'), (req, res, next) => {
  console.log('req', req.file)
  readFile(req.file.originalname)
    .then(data => {
      res.render('index', { data })
    })
})

app.use('*', (req, res) => {
  res.status(500).send('Something went wrong!')
})

const Warning = function(timestamp, id, severity, component) {
  this.satelliteId = id
  this.severity = severity
  this.component = component
  this.timestamp = timestamp
}

const calculateSeverity = (component, high, low, reading) => {
  if(component === 'BATT') {
    return low > reading 
  } else if(component === 'TSTAT') {
    return high < reading 
  }
}

app.listen(PORT, () => {
  console.log(`server listening on ${PORT}`)
}) 
