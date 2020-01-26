# Spotify Streaming

Written by @STRRL .

Trying to streaming Spotify audio for unsupported devices and players.

In order to enhance the audio output, NO PIRACY.

## Usage

- Run Program by using `java -Dserver.port=<PORT> -jar ./<PROGRAM>` first, this will extract config file:`config.toml` and exit. (First Time)
- Change the config in `config.toml`, and ENSURE `player.output` is set to `PIPE`, ENSURE `player.pipe` is set to a writable location which will finally be a pipe file. (First Time)
- Re-run program using the same command. Start up Spotify Client and take control of whatever you want.
- According to console output, insert the address to your most favorite player (Like FB2K) and do whatever you want. 

## License

 spotify-streaming
 Copyright (C) 2020 STRRL & kmahyyg
 
 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU Affero General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.
 
 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU Affero General Public License for more details.
 
 You should have received a copy of the GNU Affero General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.

