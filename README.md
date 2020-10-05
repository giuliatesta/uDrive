# uDrive

La app si occupa di valutare la guida. Per ora gestisce solo il sensore legato all'accelerometro. 
Nel package accelerometer, ci sono tutte le classi necessarie per la gestione dell'accelerometro. In particolare, AccelerometerDataEvent che rappresenta l'evento di modifica dei valori dell'accelerometro e AccelerometerDataEventListener che è una interfaccia per la gestione di questi eventi.
Nel package principale, oltre alle classi che rappresentano le activity, è presente DataManager che implementa l'interfaccia listener per i Sensor event. Nel suo metodo onSensorChanged viene chiamato il metodo onDataChanged dell'interfaccia AccelerometerDataEventListener, che calcola che tipo di evento è avvenuto. E' presente anche DataProcessor che si occupa nel particolare di ottenere le informazioni necessarie per l'UI a partire dai valori dell'accelerometro. Si occupa di fare l'analisi dei dati raccolti. 


