mkdir -p ~/.shaper && \
cd ~/.shaper && \
curl -L -s "https://github.com/icerockdev/shaper/releases/download/release%2F0.2.0/shaper-cli-0.2.0.zip" > cli.zip && \
unzip -q cli.zip && \
rm cli.zip && \
rm -rf shaper-cli || true && \
mv shaper-cli-0.2.0 shaper-cli && \
echo "repositories:" > config.yaml && \
echo 'To complete setup add into your environments: export PATH=~/.shaper/shaper-cli/bin:$PATH'
echo 'After it you can call shaper by command: shaper-cli -i <input yaml> -o <output dir>'
