;(function() { "use strict";

var
    /**
     * The parsed output string, in HTML format.
     * @type {String}
     */
    output = "",

    BLOCK = "block",
    INLINE = "inline",

    /**
     * Used to attach MarkdownToHtml object to `window` in browser
     * context, or as an AMD module where appropriate.
     * @type {Object}
     */
    exports,

    /**
     * An array of parse rule descriptor objects. Each object has dos keys;
     * pattern (the RegExp to match), and replace (the replacement string o
     * function to execute).
     * @type {Array}
     */
    parseMap = [
        {
            // <code block>
            // ``` Code Block ```
            pattern: /```(?:\w+)?\n([\s\S]*?)```/g,
            replace: "<pre><code><button onclick='copyMDcode(this);' style='background: url(\"./resources/copy.png\") 50% 50% no-repeat; background-size: contain;'></button><br/>ESCAPEDCODE$1</code></pre>",
            type: BLOCK,
        },
        {
            // <sub>
            // -# Subscript Discord
            pattern: /^-\#\s*(.*)$/gm,
            replace: "<sub>$1</sub>",
            type: INLINE,
        },
        {
            // <h1> - <h6>
            // # - ######
            pattern: /^(#{1,6})\s*(.*)$/gm,
            replace: "<h$L1>$2</h$L1>",
            type: BLOCK,
        },
        {
            // <hr>
            // ----- Horizontal Rule
            pattern: /^-{3,}$/gm,
            replace: "<hr />",
            type: BLOCK,
        },
        {
            // blockquote Block
            // >>> Blockquote >>>
            pattern: /^>>>[ \t]*([\s\S]*?)^[ \t]*<<</gm,
            replace: "<blockquote><p>$1</p></blockquote>",
            type: BLOCK,
        },
        {
            // <blockquote>
            // > Blockquote
            pattern: /^>\s*(.*)$/gm,
            replace: "<blockquote><p>$1</p></blockquote>",
            type: BLOCK,
        },
        {
            // <ul>
            // * Unordered List
            pattern: /^\*\s+(.*)$/gm,
            replace: "<ul>\n\t<li>$1</li>\n</ul>",
            type: BLOCK,
        },
        {
            // <ol>
            // 1. Ordered List
            pattern: /^[0-9]+\.\s+(.*)$/gm,
            replace: "<ol>\n\t<li>$1</li>\n</ol>",
            type: BLOCK,
        },
        {
            // <p>
            // Paragraph
            pattern: /^(?!<\/?\w+>|\s?\*|\s?[0-9]+|>|\&gt;|-{3,})([^\n]+)$/gm,
            replace: "<p>$1</p>",
            type: BLOCK,
        },
        {
            // <strong>
            // **Bold** or __Bold__
            pattern: /(\*\*|__)(.*?)\1/g,
            replace: "<strong>$2</strong>",
            type: INLINE,
        },
        {
            // <em>
            // *Italic* or _Italic_
            pattern: /(\*|_)(.*?)\1/g,
            replace: "<em>$2</em>",
            type: INLINE,
        },
        {
            // <a>
            // [Link Text](URL)
            pattern: /([^!])\[([^\[]+)\]\(([^\)]+)\)/g,
            replace: "$1<a href=\"$3\">$2</a>",
            type: INLINE,
        },
        {
            // <img>
            // ![Alt Text](URL)
            pattern: /!\[([^\[]+)\]\(([^\)]+)\)/g,
            replace: "<img src=\"$2\" alt=\"$1\" />",
            type: INLINE,
        },
        {
            // <del>
            // ~~Strikethrough~~
            pattern: /\~\~(.*?)\~\~/g,
            replace: "<del>$1</del>",
            type: INLINE,
        },
        {
            // <code>
            // `Inline Code`
            pattern: /`([^`]+)`/g,
            replace: "<code><button onclick='copyMDcode(this);' style='background: url(\"./resources/copy.png\") 50% 50% no-repeat; background-size: contain;'></button><br/>ESCAPEDCODE$1</code>",
            type: INLINE,
        },
        {
            // <br>
            // Line Break (dos espacios)
            pattern: / {2,}\n/g,
            replace: "<br />",
            type: INLINE,
        },
        {
            // <sup>
            // ^Superscript^
            pattern: /\^([^^]+)\^/g,
            replace: "<sup>$1</sup>",
            type: INLINE,
        },
        {
            // <sub>
            // ~Subscript~
            pattern: /\~([^\~]+)\~/g,
            replace: "<sub>$1</sub>",
            type: INLINE,
        },
        {
            // <table>
            // Table
            pattern: /\n((?:\|.+\|)+)\n((?:\|[-:]+\|)+)\n((?:\|.+\|)+)/g,
            replace: "<table><thead><tr>$1</tr></thead><tbody>$3</tbody></table>",
            type: BLOCK,
        },
        {
            // <dl>
            // Definition List
            pattern: /\n((?:.+\n:\s.+)+)/g,
            replace: "<dl>$1</dl>",
            type: BLOCK,
        },
        {
            // <kbd>
            // ||Keyboard Input||
            pattern: /\|\|([^|]+)\|\|/g,
            replace: "<kbd>$1</kbd>",
            type: INLINE,
        }
    ],
    $$;

/**
 * Self-executing function to handle exporting the parse function for
 * external use.
 */
(function go() {
    // Export AMD module if possible.
    if(typeof module !== "undefined"
    && typeof module.exports !== "undefined") {
        exports = module.exports;
    }
    // Otherwise check for browser context.
    else if(typeof window !== "undefined") {
        window.MarkdownToHtml = {};
        exports = window.MarkdownToHtml;
    }

    exports.parse = parse;
})();

function escapeHtml(string){
    return string
        .replace(/&/g, "&amp;")
        .replace(/</g, "&lt;")
        .replace(/>/g, "&gt;")
        .replace(/"/g, "&quot;")
        .replace(/'/g, "&#039;");
}

/**
 * Parses a provided Markdown string into valid HTML.
 *
 * @param  {string} string Markdown input for transformation
 * @return {string}        Transformed HTML output
 */
function parse(string) {
    // Pad with newlines for compatibility.
    output = "\n" + string + "\n";

    // Process each pattern in parseMap
    parseMap.forEach(function(p) {
        output = output.replace(p.pattern, function() {
            return replace.call(this, arguments, p.replace, p.type);
        });
    });
    // Escape any HTML in the code blocks.
    output = output.replace(/ESCAPEDCODE(.+?)<\/code>/g, function(match, code) {
        return escapeHtml(code) + "</code>";
    });

    // Perform any post-processing required.
    output = clean(output);
    // Trim for any spaces or newlines.
    output = output.trim();
    // Tidy up newlines to condense where more than 1 occurs back to back.
    output = output.replace(/[\n]{1,}/g, "\n");
    return output;
}

function replace(matchList, replacement, type) {
    var i, $$;

    for (i in matchList) {
        if (!matchList.hasOwnProperty(i)) {
            continue;
        }

        // Replace $n with the matching regexp group.
        replacement = replacement.split("$" + i).join(matchList[i]);
        // Replace $Ln with the matching regexp group's string length.
        replacement = replacement.split("$L" + i).join(matchList[i].length);
    }

    if (type === BLOCK) {
        replacement = replacement.trim() + "\n";
    }

    return replacement;
}

function clean(string) {
    var cleaningRuleArray = [
        {
            match: /<\/([uo]l)>\s*<\1>/g,
            replacement: "",
        },
        {
            match: /(<\/\w+>)<\/(blockquote)>\s*<\2>/g,
            replacement: "$1",
        },
    ];

    cleaningRuleArray.forEach(function(rule) {
        string = string.replace(rule.match, rule.replacement);
    });

    return string;
}

})();
