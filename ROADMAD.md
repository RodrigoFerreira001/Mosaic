- [ ] BottomSheets
- [ ] Navigation + NavController
- [ ] Dialogs
- [ ] Snackbars
- [ ] Drawer
- [ ] Text decoration
- [ ] Local image
- [ ] Images
- [ ] Improve TilesUIStateFlow
- [ ] Complete ScreenBehaviors
- [ ] Pagination
- [ ] UIData / NavigationData, FormData
- [ ] For every Compose, make the title as close as possible, it means, bring to the tile every Compose property
- [ ] Navigation3 popUpTo
- [ ] Offline Screens
- [ ] AnimatedTile
- [ ] CountDownStepperEvent
- [ ] Triggers Globais (OnDeviceNetworkChange)
- [ ] Criar um Modifier clickable que coloca Pointer.Hand
- [ ] Mecanismo de permissão
- [ ] Ajustar update/onEvent para evitar falsos positivos
- [ ] Focar na dsl do backend
- [ ] Implementar mais triggers para os events e tiles
- [ ] LocalStorage (separado por buckets)
- [ ] NextPageEvent (Pager e afins)
- [ ] Send platform info on network calls
- [ ] Events: GetFile, SaveFile, DeleteFile
- [ ] Events: CountDownTimer, StartInfiniteClock, FinishInfiniteClock
- [ ] Events: Validate (o de operações), Transformation, CheckIfHasInternetConnection
- [ ] Compactar dados entre cliente e servidor
- [ ] AsyncTiles (Client + Server, com Builders que retornar lista de tiles)
- [ ] Triggers de Scroll (OnScroll[Up, Down, Left, Right])
- [ ] Usar ids para BS e outros overlays

# Prioridades
- [ ] Fazer teste inicial do server + cliente (com actions de GetScreen)
- [ ] Adicionar todos os tiles restantes (pelo menos o contrato)
- [ ] Mudar tudo o que EventSchema e TileSchema que use List<> para utilizar ImmutableList<>
- [ ] Revisar mecanismo de cache / offline
- [ ] Animação entre telas
- [ ] Padronizar logs de erros de eventos
- [ ] Ajustar triggers de dentro de loops UpdateTilesEventRunner.kt

# Bugs
- [ ] Configuração está sendo perdida com config change
- [ ] Lazy tile sendo recarregado todas as vezes quando ocorre navegação

# Now
- [ ] Edge to edge
- [ ] Adaptive layout (maybe upgrade compose)