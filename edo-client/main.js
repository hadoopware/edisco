const {app, BrowserWindow} = require('electron')
const { globalShortcut } = require('electron')
const path = require('path')
const url = require('url')

// Keep a global reference of the window object, if you don't, the window will
// be closed automatically when the JavaScript object is garbage collected.
let win

// This method will be called when Electron has finished
// initialization and is ready to create browser windows.
// Some APIs can only be used after this event occurs.
app.on('ready', () => {
  var hotkey = 'CommandOrControl+Super+T';

  // global hotkey
  var ret = globalShortcut.register(hotkey, () => {
    console.log(hotkey+' intercepted');
    win.show();
  })

  if (!ret) {
    console.log('Hotkey registration failed')
  }

  var ret = globalShortcut.register('Escape', function(){
    //console.log('Escape is pressed');
    win.close();
  });

  if (!ret) {
    console.log('Esc registration failed')
  }
 
  // Check whether a shortcut is registered.
  console.log(globalShortcut.isRegistered(hotkey))
 
  // Create the browser window.
  win = new BrowserWindow({width: 250, height: 130, frame: false})

  // and load the index.html of the app.
  win.loadURL(url.format({
    pathname: path.join(__dirname, 'index.html'),
    protocol: 'file:',
    slashes: true
  }))

  // Open the DevTools.
  //win.webContents.openDevTools()

  // prevent closing, hide
  win.on('close', (event) => {
    event.preventDefault();
    win.hide();
  });

});


app.on('will-quit', () => {
  // Unregister all shortcuts.
  globalShortcut.unregisterAll()
})

function exit() {
  // On macOS it is common for applications and their menu bar
  // to stay active until the user quits explicitly with Cmd + Q
  if (process.platform !== 'darwin') {
    app.quit()
  }
}
   

// Quit when all windows are closed.
app.on('window-all-closed', () => {
  exit();
  // prevent exiting
  //event.preventDefault();
})

app.on('activate', () => {
  // On macOS it's common to re-create a window in the app when the
  // dock icon is clicked and there are no other windows open.
  if (win === null) {
    createWindow()
  }
})

// In this file you can include the rest of your app's specific main process
// code. You can also put them in separate files and require them here.
