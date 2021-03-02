   void optionenAnzeigen(String fxmlFile) {
        try {  // try-catch ist notwendig, um Fehler durch fehlende Dateien abzufangen.
		
            // 		
            if(hauptbereich.getChildren().size()>1) {
                viewer.back();  //
                hauptbereich.getChildren().remove(1);  //
            }

			//
            FXMLLoader loader = new FXMLLoader( 
                     getClass().getResource("view/"+fxmlFile));
            loader.setController(this);
            VBox filterFenster = (VBox) loader.load();

			//
            hauptbereich.getChildren().add(filterFenster);
        } 
        catch(Exception e)    {
            System.out.println(e);
        }     
  }