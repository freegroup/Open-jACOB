VERSION 5.00
Begin VB.Form CTITest 
   Caption         =   "jACOB CTI Emulator"
   ClientHeight    =   2535
   ClientLeft      =   165
   ClientTop       =   495
   ClientWidth     =   5400
   Icon            =   "Form1.frx":0000
   LinkTopic       =   "Form1"
   Picture         =   "Form1.frx":12FA
   ScaleHeight     =   2535
   ScaleWidth      =   5400
   StartUpPosition =   3  'Windows-Standard
   Begin VB.CommandButton Command1 
      Caption         =   "übermitteln"
      Height          =   375
      Left            =   3960
      TabIndex        =   2
      Top             =   2040
      Width           =   1095
   End
   Begin VB.TextBox phone 
      Height          =   285
      Left            =   1800
      TabIndex        =   0
      Text            =   "61540"
      Top             =   2040
      Width           =   1935
   End
   Begin VB.Label Label1 
      BackStyle       =   0  'Transparent
      Caption         =   "Telefonnr:"
      ForeColor       =   &H8000000E&
      Height          =   255
      Left            =   960
      TabIndex        =   1
      Top             =   2040
      Width           =   855
   End
   Begin VB.Menu mnuPopup 
      Caption         =   "Server"
      Visible         =   0   'False
      Begin VB.Menu mnuServer 
         Caption         =   "Server"
      End
      Begin VB.Menu mnuApp 
         Caption         =   "Applikation"
      End
      Begin VB.Menu mnuEntry 
         Caption         =   "entryPoint"
      End
      Begin VB.Menu mnuUser 
         Caption         =   "User"
      End
      Begin VB.Menu mnuPwd 
         Caption         =   "Pwd"
      End
   End
End
Attribute VB_Name = "CTITest"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = False
Dim ServerURL As String
Dim user As String
Dim pwd As String
Dim entryPoint As String
Dim app As String
Dim mnuFile As String


Private Sub Command1_Click()
    Set TheBrowser = CreateObject("InternetExplorer.Application")
    TheBrowser.Visible = True
    sUrl = ServerURL + "/enter?entry=" + entryPoint + "&app=" + app + "&user=" + user + "&pwd=" + pwd + "&phone=" + phone.Text
    TheBrowser.Navigate URL:=sUrl
End Sub

Private Sub Form_Load()
    ServerURL = "http://localhost:8070/jacob"
    user = "achim"
    pwd = "achim"
    entryPoint = "CTI"
    app = "caretaker"
    
End Sub

Private Sub mnuApp_Click()
    app = InputBox("Applikationsname", "CTI Emulator", app)
End Sub

Private Sub mnuEntry_Click()
    entryPoint = InputBox("EntryPoint", "CTI Emulator", entryPoint)
End Sub

Private Sub mnuPwd_Click()
    pwd = InputBox("Benutzerpasswort", "CTI Emulator", pwd)
End Sub

Sub mnuServer_Click()
   ServerURL = InputBox("Server URL", "CTI Emulator", ServerURL)
End Sub

Private Sub Form_MouseUp(Button As Integer, Shift As _
   Integer, X As Single, Y As Single)
   If Button = 2 Then   ' Check if right mouse button
                        ' was clicked.
      PopupMenu mnuPopup   ' Display the File menu as a
                        ' pop-up menu.
   End If
End Sub





Private Sub mnuUser_Click()
    user = InputBox("Benutzername", "CTI Emulator", user)
End Sub
