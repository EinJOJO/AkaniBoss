# Akani Boss System
Ermöglicht das Erstellen von Bosskämpfen:


### Funktionen 
- Boss-Raum Templates
- Schlüsselsystem (Entrance-Checks)
- Bosseingangs Hologramme


### Requirements
Können genutzt werden, um Bossräume zu beschränken.

- unlocked_with_key: Bossraum muss vorher mit einem Schlüssel geöffnet werden. | Standardmäßig an.
- min_level: Mindestlevel, um den Boss zu betreten. Nutzt ``player.getLevel()`` zur Abfrage.
- min_players: Mindestanzahl an Spielern in der Nähe des Bosses
- permission: Kann genutzt werden, um Bossräume nur für bestimmte Spielergruppen freizuschalten.
#### Parameter Definition:
````
<...> - Pflicht
[...] - Optional

min_level:<zahl:int>
min_players:<anzahl:int>:[radius:int]
permission:<permission:string>:[denyMessage:minimessage]
````