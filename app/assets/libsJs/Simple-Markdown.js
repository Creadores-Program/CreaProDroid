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
     * An array of parse rule descriptor objects. Each object has two keys;
     * pattern (the RegExp to match), and replace (the replacement string or
     * function to execute).
     * @type {Array}
     */
    parseMap = [
        {
            // <h1> - <h6>
            // # - ######
            pattern: /(#{1,6})([^\n]+)/g,
            replace: "<h$1>$2</h$1>",
            type: BLOCK,
        },
        {
            // <p>
            // Paragraph
            pattern: /\n(?!<\/?\w+>|\s?\*|\s?[0-9]+|>|\&gt;|-{5,})([^\n]+)/g,
            replace: "<p>$1</p>",
            type: BLOCK,
        },
        {
            // <blockquote>
            // > Blockquote
            pattern: /\n(?:&gt;|\>)\W*(.*)/g,
            replace: "<blockquote><p>$1</p></blockquote>",
            type: BLOCK,
        },
        {
            // <ul>
            // * Unordered List
            pattern: /\n\s?\*\s*(.*)/g,
            replace: "<ul>\n\t<li>$1</li>\n</ul>",
            type: BLOCK,
        },
        {
            // <ol>
            // 1. Ordered List
            pattern: /\n\s?[0-9]+\.\s*(.*)/g,
            replace: "<ol>\n\t<li>$1</li>\n</ol>",
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
            pattern: /`(.*?)`/g,
            replace: "<code>$1</code>",
            type: INLINE,
        },
        {
            // <hr>
            // ----- Horizontal Rule
            pattern: /\n-{5,}\n/g,
            replace: "<hr />",
            type: BLOCK,
        },
        {
            // <code block>
            // ``` Code Block ```
            pattern: /```([^```]+)```/g,
            replace: "<pre><code>$1</code></pre>",
            type: BLOCK,
        },
        {
            // <pre>
            // Indented Code Block
            pattern: /(?:\n\n|^)(( {4}|\t).*\n+)/g,
            replace: "<pre><code>$1</code></pre>",
            type: BLOCK,
        },
        {
            // <br>
            // Line Break (two spaces)
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

/**
 * Parses a provided Markdown string into valid HTML.
 *
 * @param  {string} string Markdown input for transformation
 * @return {string}        Transformed HTML output
 */
function parse(string) {
    // Pad with newlines for compatibility.
    output = "\n" + string + "\n";

    parseMap.forEach(function(p) {
        // Replace all matches of provided RegExp pattern with either the
        // replacement string or callback function.
        output = output.replace(p.pattern, function() {
            // console.log(this, arguments);
            return replace.call(this, arguments, p.replace, p.type);
        });
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
    var
        i,
    $$;

    for(i in matchList) {
        if(!matchList.hasOwnProperty(i)) {
            continue;
        }

        // Replace $n with the matching regexp group.
        replacement = replacement.split("$" + i).join(matchList[i]);
        // Replace $Ln with the matching regexp group's string length.
        replacement = replacement.split("$L" + i).join(matchList[i].length);
    }

    if(type === BLOCK) {
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
