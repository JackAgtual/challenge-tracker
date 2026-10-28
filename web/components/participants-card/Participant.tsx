import { cn } from "@/lib/utils";
import { components } from "@/types/api";
import Link from "next/link";
import { User } from "../User";
import { Badge } from "../ui/badge";

type Props = {
  participant: components["schemas"]["ParticipantResponse"];
  challengeIsPending: boolean;
  selected: boolean;
};

export function Participant({
  participant,
  challengeIsPending,
  selected,
}: Props) {
  return (
    <Link
      className={cn(
        "flex gap-x-4 w-fit items-center border-2 rounded-xl p-2",
        selected ? "border-black" : "border-transparent"
      )}
      href={`?participant=${participant.id}`}
    >
      <User user={participant.user} />
      {challengeIsPending &&
        (participant.ready ? (
          <Badge className="bg-green-50 text-green-700 dark:bg-green-950 dark:text-green-300">
            Ready
          </Badge>
        ) : (
          <Badge variant="outline">Not ready</Badge>
        ))}
    </Link>
  );
}
