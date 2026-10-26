import { Button } from "@/components/ui/button";
import Link from "next/link";

type Props = {
  challengeId: number;
};

export function EditChallengeButton({ challengeId }: Props) {
  return (
    <Button
      render={<Link href={`/challenges/${challengeId}/edit`} />}
      nativeButton={false}
    >
      Edit challenge
    </Button>
  );
}
