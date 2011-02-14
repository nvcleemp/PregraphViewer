# package_windows.nsi
# =========================================================================


# build script for Windows installer
!include "MUI.nsh"

Name "PregraphViewer -- Visualizer for pregraphs"

InstallDir $PROGRAMFILES\PregraphViewer

InstallDirRegKey HKLM "Software\NSIS_PregraphViewer" "Install_Dir"

#install macro's
Page custom DetectJRE
!insertmacro MUI_PAGE_COMPONENTS
!insertmacro MUI_PAGE_DIRECTORY
!insertmacro MUI_PAGE_INSTFILES
  !define MUI_FINISHPAGE_NOAUTOCLOSE
  !define MUI_FINISHPAGE_RUN
  !define MUI_FINISHPAGE_RUN_FUNCTION "LaunchLink"
#

# build script for windows installer
!insertmacro MUI_PAGE_FINISH

#uninstall macro's
!insertmacro MUI_UNPAGE_CONFIRM
!insertmacro MUI_UNPAGE_INSTFILES

#languages
!insertmacro MUI_LANGUAGE "English"

Section "-PregraphViewer"

  SetOutPath $INSTDIR

  File "../../dist/windows/PregraphViewer.exe"
  File "../../dist/windows/PregraphViewer.jar"
  File "../../COPYRIGHT.txt"
  File "../../LICENSE.txt"
  File /r "../../dist/windows/lib"
  File /r "../../dist/windows/graphfiles"

  WriteRegStr HKLM SOFTWARE\NSIS_PG "Install_Dir" "$INSTDIR"

  # write Windows Add/Remove uninstall information
  WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\PregraphViewer" "DisplayName" "PregraphViewer -- Visualizer for pregraphs"
  WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\PregraphViewer" "UninstallString" '"$INSTDIR\uninstall.exe"'
  WriteRegStr HKLM Software\Microsoft\Windows\CurrentVersion\Uninstall\PregraphViewer" "DisplayVersion" "1.0"
  WriteRegDWORD HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\PregraphViewer" "NoModify" 1
  WriteRegDWORD HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\PregraphViewer" "NoRepair" 1

  WriteRegStr HKU "Software\JavaSoft\Prefs\be\ugent\caagt\nvcleemp\pregraph\viewer\preferences" "gui.currentdir" "$INSTDIR\graphfiles"
  WriteUninstaller "uninstall.exe"

SectionEnd


Section "Start Menu Shortcuts"

  CreateDirectory "$SMPROGRAMS\PregraphViewer"
  CreateShortCut "$SMPROGRAMS\PregraphViewer\Uninstall.lnk" "$INSTDIR\uninstall.exe" "" "$INSTDIR\uninstall.exe" 0
  CreateShortCut "$SMPROGRAMS\PregraphViewer\PregraphViewer.lnk" "$INSTDIR\PregraphViewer.exe" "" "$INSTDIR\PregraphViewer.exe" 0
   
SectionEnd

Section "Uninstall"
  
  # Remove registry keys
  DeleteRegKey HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\PregraphViewer"
  DeleteRegKey HKLM SOFTWARE\NSIS_PregraphViewer
  DeleteRegKey HKU "Software\JavaSoft\Prefs\be\ugent\caagt\nvcleemp\pregraph\viewer\preferences"
  DeleteRegKey /ifempty HKU "Software\JavaSoft\Prefs\be\ugent\caagt\nvcleemp\pregraph\viewer"
  DeleteRegKey /ifempty HKU "Software\JavaSoft\Prefs\be\ugent\caagt\nvcleemp\pregraph"
  DeleteRegKey /ifempty HKU "Software\JavaSoft\Prefs\be\ugent\caagt\nvcleemp"
  DeleteRegKey /ifempty HKU "Software\JavaSoft\Prefs\be\ugent\caagt"
  DeleteRegKey /ifempty HKU "Software\JavaSoft\Prefs\be\ugent"
  DeleteRegKey /ifempty HKU "Software\JavaSoft\Prefs\be"

  # Remove files and uninstaller
  Delete $INSTDIR\PregraphViewer.exe
  Delete $INSTDIR\PregraphViewer.jar
  Delete $INSTDIR\uninstall.exe
  Delete $INSTDIR\COPYRIGHT.txt
  Delete $INSTDIR\LICENSE.txt
  RMDir /r /REBOOTOK "$INSTDIR\libs\"
  RMDir /r /REBOOTOK "$INSTDIR\graphfiles\"

  # Remove shortcuts, if any
  Delete "$SMPROGRAMS\PregraphViewer\*.*"

  # Remove directories used
  RMDir /REBOOTOK "$SMPROGRAMS\PregraphViewer"
  RMDir /REBOOTOK "$INSTDIR"

SectionEnd

Function LaunchLink
  ExecShell "" "$INSTDIR\PregraphViewer.exe"
FunctionEnd

Function DetectJRE
  ReadRegStr $2 HKLM "SOFTWARE\JavaSoft\Java Runtime Environment" \
             "CurrentVersion"
  StrCmp $2 "1.5" done
  StrCmp $2 "1.6" done
  
  Call JRENotFound
  
  done:
FunctionEnd

Function JRENotFound
        MessageBox MB_OK "PregraphViewer uses Java 5.0 or higher, please download it \
                         from www.java.com"
        Quit
FunctionEnd

Function .onInit
 
  ReadRegStr $R0 HKLM \
  "Software\Microsoft\Windows\CurrentVersion\Uninstall\PregraphViewer" \
  "UninstallString"
  StrCmp $R0 "" done
 
  MessageBox MB_OKCANCEL|MB_ICONEXCLAMATION \
  "PregraphViewer is already installed. $\n$\nClick `OK` to remove the \
  previous version or `Cancel` to cancel this upgrade." \
  IDOK uninst
  Abort
  
#run the uninstaller
uninst:
  ClearErrors
  Exec $INSTDIR\uninstall.exe
  
done:
 
FunctionEnd
