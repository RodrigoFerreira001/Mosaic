- [ ] BottomSheets
- [ ] Navigation + NavController
- [ ] Dialogs
- [ ] Snackbars
- [ ] Drawer
- [ ] Text decoration
- [x] Local image
- [x] Images
- [x] Improve TilesUIStateFlow
- [x] Complete ScreenBehaviors
- [x] Pagination
- [x] UIData / NavigationData, FormData
- [ ] For every Compose, make the title as close as possible, it means, bring to the tile every Compose property
- [x] Navigation3 popUpTo
- [x] Offline Screens
- [ ] AnimatedTile
- [ ] CountDownStepperEvent
- [ ] Triggers Globais (OnDeviceNetworkChange)
- [ ] Criar um Modifier clickable que coloca Pointer.Hand
- [?] Mecanismo de permissão
- [ ] Ajustar update/onEvent para evitar falsos positivos
- [x] Focar na dsl do backend
- [x] Implementar mais triggers para os events e tiles
- [x] LocalStorage (separado por buckets)
- [x] NextPageEvent (Pager e afins)
- [x] Send platform info on network calls
- [x] Events: GetFile, SaveFile, DeleteFile
- [ ] Events: CountDownTimer, StartInfiniteClock, FinishInfiniteClock
- [?] Events: Validate (o de operações), Transformation, CheckIfHasInternetConnection
- [ ] Compactar dados entre cliente e servidor
- [x] AsyncTiles (Client + Server, com Builders que retornar lista de tiles)
- [x] Triggers de Scroll (OnScroll[Up, Down, Left, Right])
- [ ] Usar ids para BS e outros overlays
- [ ] Color com gradiente
- [ ] Um evento que executa outro evento envolopado em um Job. Tal job é registrado e pode ser cancelado externamente através de outro evento. E o evento deve se autoremover ao finalizar

# Prioridades
- [x] Fazer teste inicial do server + cliente (com actions de GetScreen)
- [x] Adicionar todos os tiles restantes (pelo menos o contrato)
- [x] Mudar tudo o que EventSchema e TileSchema que use List<> para utilizar ImmutableList<>
- [ ] Revisar mecanismo de cache / offline
- [x] Animação entre telas
- [ ] Padronizar logs de erros de eventos
- [ ] Ajustar triggers de dentro de loops UpdateTilesEventRunner.kt

# Bugs
- [ ] Configuração está sendo perdida com config change
- [ ] Lazy tile sendo recarregado todas as vezes quando ocorre navegação

# Now
- [x] Edge to edge
- [x] Adaptive layout (maybe upgrade compose)